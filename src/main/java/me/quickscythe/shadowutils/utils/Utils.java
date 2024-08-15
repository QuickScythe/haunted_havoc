package me.quickscythe.shadowutils.utils;

import me.quickscythe.shadowcore.commands.CommandManager;
import me.quickscythe.shadowcore.utils.entity.RegistryUtils;
import me.quickscythe.shadowcore.utils.ShadowUtils;
import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.chat.MessageUtils;
import me.quickscythe.shadowcore.utils.entity.CustomEntityRegistry;
import me.quickscythe.shadowutils.HauntedHavoc;
import me.quickscythe.shadowutils.commands.HauntedHavocCommand;
import me.quickscythe.shadowutils.extras.entity.entities.HauntedEntities;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;

public class Utils {

    static HauntedHavoc plugin;
    static Logger logger;

    static HauntedOccasion occasion;
    static HavocConfig config;
    static MessageUtils messageUtils;

    static World lobby;
    static World world;

    static CustomEntityRegistry cer;

    static HauntedVoiceService voiceService;

    public static void init(HauntedHavoc plugin){
        Utils.plugin = plugin;
        logger = new Logger(plugin);
        messageUtils = new MessageUtils(plugin);

        cer = RegistryUtils.newEntityRegistry(plugin);

        HauntedEntities.init(cer);

        occasion = new HauntedOccasion(plugin, "occastion_data");
        config = new HavocConfig(plugin, "config", "config.json");

        String world = config.getData().getString("play_world");
        if(Bukkit.getWorld(world) == null){
            logger.log("Play world (" + world + ") does not exist. Creating world.");
            WorldCreator creator = new WorldCreator(world);
            Utils.world = Bukkit.createWorld(creator);
            logger.log("Created Play world (" + world + ")");
        } else Utils.world = Bukkit.getWorld(world);

        Utils.world.setGameRule(GameRule.NATURAL_REGENERATION, false);

        String lobby = config.getData().getString("lobby_world");
        if(Bukkit.getWorld(lobby) == null){
            logger.log("Lobby world (" + lobby + ") does not exist. Creating world.");
            WorldCreator creator = new WorldCreator(lobby);
            Utils.lobby = Bukkit.createWorld(creator);
            logger.log("Created Lobby world (" + lobby + ")");
        } else Utils.lobby = Bukkit.getWorld(lobby);

        Utils.lobby.setDifficulty(Difficulty.PEACEFUL);

        ShadowUtils.getTeamManager().registerTeam("spectators").setColor(NamedTextColor.GRAY);

        new CommandManager.CommandBuilder(new HauntedHavocCommand(plugin)).setAliases("hh").setDescription("Main command for Haunted Havoc").register();

        voiceService = new HauntedVoiceService();
        ShadowUtils.registerVoiceService(voiceService);

//        cer.register("test", CustomZombie.class);


    }

    public static HauntedVoiceService getVoiceService() {
        return voiceService;
    }

    public static CustomEntityRegistry getEntityRegistry() {
        return cer;
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
