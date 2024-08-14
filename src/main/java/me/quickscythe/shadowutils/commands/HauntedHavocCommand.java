package me.quickscythe.shadowutils.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.shadowcore.commands.ShadowCommand;
import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import me.quickscythe.shadowutils.entities.CustomZombie;
import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.CraftWorld;
import org.jetbrains.annotations.NotNull;

public class HauntedHavocCommand  implements ShadowCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if(args.length == 0){
            //TODO send options
            // Also check perms prob?
            return;
        }
        if(args[0].equalsIgnoreCase("teams") || args[0].equalsIgnoreCase("team")) {
            //TODO
            // /hh teams create <team>
            // /hh teams edit <team> setcolor <color>
            // /hh teams edit <team> addmember <member>
            // /hh teams edit <team> removemember <member>
            if(args.length == 1){
                Location location = stack.getLocation();
//                Utils.spawnZombie(location);
                Utils.getCustomEntityRegistry().spawn("customzombie", location);
                //TODO send list of options
                return;
            }
            if(args[1].equalsIgnoreCase("create")){
                if(args.length == 2){
                    //todo tell player they need more args
                    return;
                }
                TeamManager.registerTeam(args[2]);
                //todo tell player good job
            }
            if(args[1].equalsIgnoreCase("edit")){
                if(args.length < 5){
                    //todo show player edit list
                    return;
                }
                Team team = TeamManager.getTeam(args[2]);
                if(team == null){
                    Utils.getLogger().log(Logger.LogLevel.ERROR, "That team doesn't exist", stack.getSender());
                    return;
                }
                if(args[3].equalsIgnoreCase("setcolor")){
                    team.setColor(NamedTextColor.NAMES.valueOr(args[4].toUpperCase(), NamedTextColor.WHITE));
                }
                if(args[3].equalsIgnoreCase("addmember") || args[3].equalsIgnoreCase("addplayer") || args[3].equalsIgnoreCase("add")){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[4]);
                    team.addPlayer(player);
                }
                if(args[3].equalsIgnoreCase("removemember") || args[3].equalsIgnoreCase("removeplayer") || args[3].equalsIgnoreCase("remove")){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[4]);
                    team.removePlayer(player);
                }

            }
        }
        if(args[0].equalsIgnoreCase("setspawn")){
            ShadowUtils.getLocationManager().addLocation("spawn", stack.getLocation());
        }
        if(args[0].equalsIgnoreCase("start")){
            Utils.getLogger().log("Starting occasion");
            Utils.getOccasion().start();
        }


//        TeamManager.getTeam("red").addPlayer((OfflinePlayer) stack.getSender());
    }
}
