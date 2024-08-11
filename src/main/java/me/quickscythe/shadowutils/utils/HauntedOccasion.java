package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.utils.config.ConfigClass;
import me.quickscythe.shadowcore.utils.occasion.Occasion;
import me.quickscythe.shadowcore.utils.occasion.OccasionManager;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json2.JSONObject;
import org.json2.JSONPropertyIgnore;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HauntedOccasion extends ConfigClass implements Occasion {

    long last_check = 0;


    public HauntedOccasion(JavaPlugin plugin, String configFile) {
        super(plugin, configFile);
        if (!getConfig().getData().has("start"))
            getConfig().getData().put("start", false);
        if (!getConfig().getData().has("end"))
            getConfig().getData().put("end", false);
        if (!getConfig().getData().has("started_time"))
            getConfig().getData().put("started_time", 0);
        if (!getConfig().getData().has("started"))
            getConfig().getData().put("started", false);

        OccasionManager.registerOccasion("test", this);
    }

    @Override
    public boolean started() {
        return getConfig().getData().getBoolean("started");
    }

    @Override
    public boolean start() {

        for(Player player : Bukkit.getOnlinePlayers()){
            boolean onTeam = false;
            for(Team team : TeamManager.getTeams()){
                if(team.getPlayers().contains(player.getUniqueId())){
                    onTeam = true;
                    break;
                }
            }
            if(!onTeam){
                TeamManager.getTeam("spectators").addPlayer(player);
            }
        }

        getConfig().getData().put("started_time", new Date().getTime());
        getConfig().getData().put("started", true);

        double world_border = Utils.getConfig().getData().getDouble("world_border_max");

        Utils.getWorld().getWorldBorder().setSize(world_border);

        Random random = new Random();
        for(Team team : TeamManager.getTeams()){
            Location loc = new Location(Utils.getWorld(), random.nextInt((int) world_border), 100, random.nextInt((int) world_border));
            loc = loc.getWorld().getHighestBlockAt(loc).getLocation().clone().add(0,2,0);
            for(UUID uid : team.getPlayers()){
                if(Bukkit.getPlayer(uid) == null) continue;
                int radius = Utils.getConfig().getData().getInt("team_teleport_radius");
                Bukkit.getPlayer(uid).teleport(loc.getWorld().getHighestBlockAt(loc.clone().add(random.nextInt(radius), 0 , random.nextInt(radius))).getLocation());
            }
        }

//        for (Player player : Bukkit.getOnlinePlayers()) {
//            player.teleport(Utils.getWorld().getSpawnLocation());
//        }
        //TODO
        // teleport players randomly
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

        if (current > duration + delay) {
            if (current > delay) {
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
}
