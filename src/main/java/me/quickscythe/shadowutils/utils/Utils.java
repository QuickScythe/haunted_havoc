package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.commands.CommandManager;
import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.heartbeat.HeartbeatUtils;
import me.quickscythe.shadowutils.HauntedHavoc;
import me.quickscythe.shadowutils.commands.HauntedHavocCommand;

public class Utils {

    static HauntedHavoc plugin;
    static Logger logger;

    static HauntedOccasion occasion;
    static HavocConfig config;

    public static void init(HauntedHavoc plugin){
        Utils.plugin = plugin;
        logger = new Logger(plugin);

        occasion = new HauntedOccasion(plugin, "occastion_data");
        config = new HavocConfig(plugin, "config", "config.json");


        new CommandManager.CommandBuilder("hauntedhavoc", new HauntedHavocCommand()).setAliases("hh").setDescription("Main command for Haunted Havoc").register(plugin);

    }

    public static HavocConfig getConfig(){
        return config;
    }

    public static void finish(){
        occasion.finish();
        config.finish();
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

}
