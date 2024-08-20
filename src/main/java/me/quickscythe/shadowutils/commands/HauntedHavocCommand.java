package me.quickscythe.shadowutils.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.quickscythe.shadowcore.commands.ShadowCommand;
import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.team.Team;
import me.quickscythe.shadowutils.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.execution.CustomCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class HauntedHavocCommand extends ShadowCommand {

    public HauntedHavocCommand(JavaPlugin plugin) {
        super(plugin, "hauntedhavoc");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return Commands.literal(getName()).executes(context -> {
            //todo send message of avalible commands
            return Command.SINGLE_SUCCESS;})
                .then(argument("arg1", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            builder.suggest("teams");
                            builder.suggest("start");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String sub_cmd = StringArgumentType.getString(context, "arg1");
                            if(sub_cmd.equalsIgnoreCase("teams") || sub_cmd.equalsIgnoreCase("team")){
                                //todo send feedback
                            }
                            if(sub_cmd.equalsIgnoreCase("start") ){
                                Utils.getOccasion().start();
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(argument("arg2", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    String arg1 = context.getArgument("arg1", String.class);
                                    if(arg1.equalsIgnoreCase("teams") || arg1.equalsIgnoreCase("team")){
                                        builder.suggest("create");
                                        builder.suggest("edit");
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(argument("arg3", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            String arg1 = StringArgumentType.getString(context, "arg1");
                                            String arg2 = StringArgumentType.getString(context, "arg2");
                                            if(arg1.equalsIgnoreCase("teams") || arg1.equalsIgnoreCase("team")){
                                                if(arg2.equalsIgnoreCase("edit")){
                                                    for(Team team : ShadowUtils.getTeamManager().getTeams())
                                                        builder.suggest(team.getName());
                                                }
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String arg1 = StringArgumentType.getString(context, "arg1");
                                            String arg2 = StringArgumentType.getString(context, "arg2");
                                            if(arg1.equalsIgnoreCase("team") || arg1.equalsIgnoreCase("teams")){
                                                if(arg2.equalsIgnoreCase("create")){
                                                    String teamName = StringArgumentType.getString(context, "arg3");
                                                    ShadowUtils.getTeamManager().registerTeam(teamName);
                                                    Utils.getLogger().log(Logger.LogLevel.INFO, "Team " + teamName + " has been created", context.getSource().getSender());
                                                }
                                                if(arg2.equalsIgnoreCase("edit")){

                                                    //TODO send feedback
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })))).build();
    }

//    @Override
//    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, @NotNull String[] args) {
//        Collection<String> list = new ArrayList<>();
//        if (args.length == 0) {
//            list.add("teams");
//            list.add("spawn");
//            return list;
//        }
//        if (args[0].equalsIgnoreCase("spawn")) {
////            for (HauntedEntities e : HauntedEntities.values())
////                list.add(e.name());
//        }
//        return list;
//
//    }
//
//    @Override
//    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
//        if (args.length == 0) {
//            //TODO send options
//            // Also check perms prob?
//            return;
//        }
//        if (args[0].equalsIgnoreCase("spawn")) {
//            if (args.length == 1) {
//                stack.getSender().sendMessage(Component.text("Usage: /hh spawn <entity>"));
//                return;
//            }
////            HauntedEntities.valueOf(args[1].toUpperCase()).spawn(stack.getLocation());
//
//        }
//        if (args[0].equalsIgnoreCase("teams") || args[0].equalsIgnoreCase("team")) {
//            //TODO
//            // /hh teams create <team>
//            // /hh teams edit <team> setcolor <color>
//            // /hh teams edit <team> addmember <member>
//            // /hh teams edit <team> removemember <member>
//            if (args.length == 1) {
//                Location location = stack.getLocation();
//                Utils.getEntityRegistry().spawn("CURSED_CREEPER", location);
//                Utils.getLogger().log(Logger.LogLevel.WARN, "TEST", stack.getSender());
////                Utils.spawnZombie(location);
////                HauntedEntities.HAUNTED_ARMOR.spawn(location);
////                Utils.getCustomEntityRegistry().spawn("cursedcreeper", location);
//                //TODO send list of options
//                return;
//            }
//            if (args[1].equalsIgnoreCase("create")) {
//                if (args.length == 2) {
//                    //todo tell player they need more args
//                    return;
//                }
//                ShadowUtils.getTeamManager().registerTeam(args[2]);
//                //todo tell player good job
//            }
//            if (args[1].equalsIgnoreCase("edit")) {
//                if (args.length < 5) {
//                    //todo show player edit list
//                    return;
//                }
//                Team team = ShadowUtils.getTeamManager().getTeam(args[2]);
//                if (team == null) {
//                    Utils.getLogger().log(Logger.LogLevel.ERROR, "That team doesn't exist", stack.getSender());
//                    return;
//                }
//                if (args[3].equalsIgnoreCase("setcolor")) {
//                    team.setColor(NamedTextColor.NAMES.valueOr(args[4].toUpperCase(), NamedTextColor.RED));
//                }
//                if (args[3].equalsIgnoreCase("addmember") || args[3].equalsIgnoreCase("addplayer") || args[3].equalsIgnoreCase("add")) {
//                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[4]);
//                    team.addPlayer(player);
//                }
//                if (args[3].equalsIgnoreCase("removemember") || args[3].equalsIgnoreCase("removeplayer") || args[3].equalsIgnoreCase("remove")) {
//                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[4]);
//                    team.removePlayer(player);
//                }
//
//            }
//        }
//        if (args[0].equalsIgnoreCase("setspawn")) {
//            ShadowUtils.getLocationManager().addLocation("spawn", stack.getLocation());
//        }
//        if (args[0].equalsIgnoreCase("start")) {
//            Utils.getLogger().log("Starting occasion");
//            Utils.getOccasion().start();
//        }


//        TeamManager.getTeam("red").addPlayer((OfflinePlayer) stack.getSender());
//    }
}
