package me.quickscythe.shadowutils.listeners;

import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import me.quickscythe.shadowutils.extras.blood.BloodSplat;
import me.quickscythe.shadowutils.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
    public void onEntityDamage(EntityDamageEvent e){
        new BloodSplat(e.getEntity().getLocation(), e.getDamage());
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player player){
            if(Utils.getOccasion().inGracePeriod()) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Team t = null;
        for(Team team : TeamManager.getTeams()){
            if(team.getPlayers().contains(e.getPlayer().getUniqueId())){
                t = team;
                break;
            }
        }
        if(t != null){
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            t.removePlayer(e.getPlayer());
            TeamManager.getTeam("spectators").addPlayer(e.getPlayer());
        }

    }
}
