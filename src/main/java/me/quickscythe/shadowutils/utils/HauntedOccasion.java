package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.utils.Jsonifier;
import me.quickscythe.shadowcore.utils.config.ConfigClass;
import me.quickscythe.shadowcore.utils.occasion.Occasion;
import me.quickscythe.shadowcore.utils.occasion.OccasionManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json2.JSONObject;
import org.json2.JSONPropertyIgnore;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HauntedOccasion extends ConfigClass implements Occasion {


    public HauntedOccasion(JavaPlugin plugin, String configFile) {
        super(plugin, configFile);
        if(!getConfig().getData().has("start"))
            getConfig().getData().put("start", false);
        if(!getConfig().getData().has("end"))
            getConfig().getData().put("end", false);
        if(!getConfig().getData().has("started_time"))
            getConfig().getData().put("started_time", 0);
        if(!getConfig().getData().has("started"))
            getConfig().getData().put("started", false);

        OccasionManager.registerOccasion("test", this);
    }

    @Override
    public boolean started() {
        return getConfig().getData().getBoolean("started");
    }

    @Override
    public boolean start() {
        getConfig().getData().put("started_time", new Date().getTime());
        getConfig().getData().put("started", true);

        return true;
    }

    @Override
    public boolean check() {
        long started = getConfig().getData().getLong("started_time");
        long duration = TimeUnit.MILLISECONDS.convert(Utils.getConfig().getData().getLong("session_time"), TimeUnit.MINUTES);
        long now = new Date().getTime();
        long current = now - started;
        long remaining = duration - current;
        Utils.getLogger().log("Started: " + started);
        Utils.getLogger().log("Duration: " + duration);
        Utils.getLogger().log("Now: " + now);
        Utils.getLogger().log("Current: " + current);
        Utils.getLogger().log("Remaining: " + remaining);
        return false;
    }

    @Override
    public boolean end() {
        return getConfig().getData().getBoolean("end");
    }

    @Override
    @JSONPropertyIgnore
    public JSONObject toJson() {
        return getConfig().getData();
    }
}
