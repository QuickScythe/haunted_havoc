package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.utils.config.ConfigClass;
import me.quickscythe.shadowcore.utils.config.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.json2.JSONObject;

public class HavocConfig extends ConfigClass {
    public HavocConfig(JavaPlugin plugin, String configFile, String resource) {
        super(plugin, configFile, resource);
    }

    public JSONObject getData(){
        return CONFIG.getData();
    }
}
