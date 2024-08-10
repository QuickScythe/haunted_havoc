package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowutils.HauntedHavoc;

public class Utils {

    static HauntedHavoc plugin;
    static Logger logger;

    public static void init(HauntedHavoc plugin){
        Utils.plugin = plugin;
        logger = new Logger(plugin);

    }

    public static HauntedHavoc getPlugin(){
        return plugin;
    }

    public static Logger getLogger(){
        return logger;
    }

}
