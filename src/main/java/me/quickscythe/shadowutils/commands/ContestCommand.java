package me.quickscythe.shadowutils.commands;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.shadowcore.commands.ShadowCommand;
import me.quickscythe.shadowcore.utils.team.TeamManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class ContestCommand implements ShadowCommand {
    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] strings) {

        TeamManager.getTeam("red").addPlayer((OfflinePlayer) stack.getSender());
    }
}
