package me.quickscythe.shadowutils.utils;

import de.maxhenkel.voicechat.api.Group;
import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.config.ConfigClass;
import me.quickscythe.shadowcore.utils.occasion.Occasion;
import me.quickscythe.shadowcore.utils.occasion.OccasionManager;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json2.JSONObject;
import org.json2.JSONPropertyIgnore;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HauntedOccasion extends ConfigClass implements Occasion {

    private final int MAX_PHASES = 5;
    long last_check = 0;
    private int phase = 0;


    public HauntedOccasion(JavaPlugin plugin, String configFile) {
        super(plugin, configFile);
        if (!getConfig().getData().has("end")) getConfig().getData().put("end", false);
        if (!getConfig().getData().has("started_time")) getConfig().getData().put("started_time", 0);
        if (!getConfig().getData().has("started")) getConfig().getData().put("started", false);

        OccasionManager.registerOccasion("test", this);
    }

    @Override
    public boolean started() {
        return getConfig().getData().getBoolean("started");
    }

    @Override
    public boolean start() {

        TeamManager teamManager = ShadowUtils.getTeamManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (teamManager.getTeam(player) != null) {
                teamManager.getTeam("spectators").addPlayer(player);
            }
        }

        getConfig().getData().put("started_time", new Date().getTime());
        getConfig().getData().put("started", true);

        double world_border = Utils.getConfig().getData().getDouble("world_border_max");

        Utils.getWorld().getWorldBorder().setSize(world_border);

        Random random = new Random();
        int radius = (int) (world_border / 2);
        for (Team team : teamManager.getTeams()) {
            Group vc = Utils.getVoiceService().getServerApi().groupBuilder().setType(Group.Type.OPEN).setPersistent(false).setHidden(true).setName(team.getName() + " chat").build();
            Location loc = Utils.getWorld().getWorldBorder().getCenter().clone();
            loc.add((random.nextBoolean() ? 1 : -1) * random.nextInt(radius), 0, (random.nextBoolean() ? 1 : -1) * random.nextInt(radius));
            loc = loc.getWorld().getHighestBlockAt(loc).getLocation().clone();
            while (loc.getBlock().getType().equals(Material.AIR) || loc.getBlock().getType().equals(Material.WATER)) {
                loc = Utils.getWorld().getWorldBorder().getCenter().clone();
                loc.add((random.nextBoolean() ? 1 : -1) * world_border, 0, (random.nextBoolean() ? 1 : -1) * world_border);
                loc = loc.getWorld().getHighestBlockAt(loc).getLocation().clone();
            }

            for (UUID uid : team.getPlayers()) {
                if (Bukkit.getPlayer(uid) == null) continue;
                Player player = Bukkit.getPlayer(uid);
                assert player != null;
                if (Utils.getVoiceService().getServerApi() != null)
                    Utils.getVoiceService().getServerApi().getConnectionOf(uid).setGroup(vc);
                int team_teleport_radius = Utils.getConfig().getData().getInt("team_teleport_radius");
                player.teleportAsync(loc.getWorld().getHighestBlockAt(loc.clone().add(random.nextInt(team_teleport_radius), 0, random.nextInt(team_teleport_radius))).getLocation().clone().add(0, 1, 0)).thenAccept(success -> {
                    if (success) {
                        player.sendMessage("Teleport success");
                    } else {
                        player.sendMessage("There was an error teleporting you");
                    }
                });
            }
        }

        return true;
    }

    @Override
    public boolean check() {
        long delay = TimeUnit.MILLISECONDS.convert(Utils.getConfig().getData().getInt("grace_period"), TimeUnit.MINUTES);
        long started = getConfig().getData().getLong("started_time");

        int max = Utils.getConfig().getData().getInt("world_border_max");
        int min = Utils.getConfig().getData().getInt("world_border_min");
        long duration = TimeUnit.MILLISECONDS.convert(Utils.getConfig().getData().getLong("session_time"), TimeUnit.MINUTES);

        long now = new Date().getTime();
        long current = now - started;
        long remaining = duration - current;
        long delta = now - last_check;


        double dif = (double) (max - min) / duration;
        double size = max - ((current - delay) * dif);
        if (size < 1) size = 1;

        if (current < duration + delay) {
            if (current > delay) {
                int phase = (int) (Math.floor((double) current / ((double) duration / MAX_PHASES))) - (MAX_PHASES - 1);
                if (this.phase != phase) {
                    this.phase = phase;
                    Utils.getLogger().log("Moving on to phase " + phase);
                }
                Utils.getWorld().getWorldBorder().setSize(size, TimeUnit.MILLISECONDS, delta);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(Component.text("Time Remaining: ").append(Utils.getMessageUtils().formatTime(remaining + delay)));
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendActionBar(Component.text("Delay: ").append(Utils.getMessageUtils().formatTime(delay - current)));
                }
            }
        }


        last_check = now;
        return false;
    }

    @Override
    public boolean end() {
        return getConfig().getData().getBoolean("end");
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    @JSONPropertyIgnore
    public JSONObject toJson() {
        return getConfig().getData();
    }

    public boolean inGracePeriod() {
        long delay = TimeUnit.MILLISECONDS.convert(Utils.getConfig().getData().getInt("grace_period"), TimeUnit.MINUTES);
        long started = getConfig().getData().getLong("started_time");
        long now = new Date().getTime();
        long current = now - started;
        return current < delay;
    }

    public int getPhase() {
        return phase;
    }

    public int getMoonPhase(World world) {
        return (int) ((world.getFullTime() / 24000) % 8);
    }
}
