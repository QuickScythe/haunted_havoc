package me.quickscythe.shadowutils;

import me.quickscythe.shadowutils.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class HauntedHavoc extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Utils.init(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Utils.getLogger().log("Finishing Haunted Havoc");
        Utils.finish();
    }
}
