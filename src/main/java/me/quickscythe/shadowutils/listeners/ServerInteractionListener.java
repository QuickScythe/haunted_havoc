package me.quickscythe.shadowutils.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import me.quickscythe.shadowutils.extras.blood.BloodSplat;
import me.quickscythe.shadowutils.extras.entity.CustomEntity;
import me.quickscythe.shadowutils.extras.entity.CustomZombie;
import me.quickscythe.shadowutils.extras.entity.HauntedEntities;
import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class ServerInteractionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!Utils.getOccasion().started() && !Utils.getOccasion().finished()) {

            if (ShadowUtils.getLocationManager().getLocation("spawn") != null)
                e.getPlayer().teleport(ShadowUtils.getLocationManager().getLocation("spawn"));
            else e.getPlayer().teleport(Utils.getLobby().getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (ShadowUtils.getTeamManager().getTeam(e.getPlayer()) != null) {
            Team team = ShadowUtils.getTeamManager().getTeam(e.getPlayer());
            e.message(Component.text("[" + team.getName() + "]").append(Component.text(" ").append(e.message())));
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof TextDisplay || e.getEntity() instanceof Item) return;
//        if(Utils.getOccasion().started()){
        if (new Random().nextDouble() < 0.1) if (!(((CraftEntity) e.getEntity()).getHandle() instanceof CustomEntity)) {
//            HauntedEntities.values()[HauntedEntities.values().length - 1].spawn(e.getLocation());
        }
//        }
    }

    //TODO register entity spawn event, check game and moon phase, spawn mobs depending

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof CraftZombie) {
            Utils.getLogger().log("Found craft Zom");
            if (((CraftZombie) e.getEntity()).getHandle() instanceof CustomZombie) {
                Utils.getLogger().log("Custom Zom!");
            }
        }
        new BloodSplat(e.getEntity().getLocation(), e.getDamage());
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player damager && e.getEntity() instanceof Player player) {
            if (Utils.getOccasion().inGracePeriod()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Team t = null;
        for (Team team : ShadowUtils.getTeamManager().getTeams()) {
            if (team.getPlayers().contains(e.getPlayer().getUniqueId())) {
                t = team;
                break;
            }
        }
        if (t != null) {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            t.removePlayer(e.getPlayer());
            ShadowUtils.getTeamManager().getTeam("spectators").addPlayer(e.getPlayer());
        }

    }
}
