package me.quickscythe.shadowutils.utils;

import com.mojang.brigadier.Message;
import me.quickscythe.shadowcore.commands.CommandManager;
import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.chat.MessageUtils;
import me.quickscythe.shadowcore.utils.heartbeat.HeartbeatUtils;
import me.quickscythe.shadowutils.HauntedHavoc;
import me.quickscythe.shadowutils.commands.HauntedHavocCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Utils {

    static HauntedHavoc plugin;
    static Logger logger;

    static HauntedOccasion occasion;
    static HavocConfig config;
    static MessageUtils messageUtils;

    static World lobby;
    static World world;

    public static void init(HauntedHavoc plugin){
        Utils.plugin = plugin;
        logger = new Logger(plugin);
        messageUtils = new MessageUtils(plugin);

        occasion = new HauntedOccasion(plugin, "occastion_data");
        config = new HavocConfig(plugin, "config", "config.json");

        String world = config.getData().getString("play_world");
        if(Bukkit.getWorld(world) == null){
            logger.log("Play world (" + world + ") does not exist. Creating world.");
            WorldCreator creator = new WorldCreator(world);
            Utils.world = Bukkit.createWorld(creator);
            logger.log("Created Play world (" + world + ")");
        } else Utils.world = Bukkit.getWorld(world);

        String lobby = config.getData().getString("lobby_world");
        if(Bukkit.getWorld(lobby) == null){
            logger.log("Lobby world (" + lobby + ") does not exist. Creating world.");
            WorldCreator creator = new WorldCreator(lobby);
            Utils.lobby = Bukkit.createWorld(creator);
            logger.log("Created Lobby world (" + lobby + ")");
        } else Utils.lobby = Bukkit.getWorld(lobby);

        new CommandManager.CommandBuilder("hauntedhavoc", new HauntedHavocCommand()).setAliases("hh").setDescription("Main command for Haunted Havoc").register(plugin);

    }

    public static HavocConfig getConfig(){
        return config;
    }

    public static void finish(){
        getLogger().log("Saving files");
        occasion.finish();
        config.finish();
        messageUtils.finish();
        getLogger().log("Files saved.");
    }

    public static HauntedOccasion getOccasion(){
        return occasion;
    }

    public static HauntedHavoc getPlugin(){
        return plugin;
    }

    public static Logger getLogger(){
        return logger;
    }

    public static World getLobby() {
        return lobby;
    }

    public static World getWorld(){
        return world;
    }

    public static MessageUtils getMessageUtils() {
        return messageUtils;
    }
}
