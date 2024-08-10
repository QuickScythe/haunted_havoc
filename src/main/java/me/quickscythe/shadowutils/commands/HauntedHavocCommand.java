package me.quickscythe.shadowutils.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.shadowcore.commands.ShadowCommand;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import me.quickscythe.shadowutils.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class HauntedHavocCommand  implements ShadowCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        if(args.length == 0){
            //TODO send options
            // Also check perms prob?
            return;
        }
        if(args[0].equalsIgnoreCase("start")){
            Utils.getLogger().log("Starting occasion");
            Utils.getOccasion().start();
        }


//        TeamManager.getTeam("red").addPlayer((OfflinePlayer) stack.getSender());
    }
}
