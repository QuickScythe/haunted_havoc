package me.quickscythe.shadowutils.listeners;

import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowutils.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
}
