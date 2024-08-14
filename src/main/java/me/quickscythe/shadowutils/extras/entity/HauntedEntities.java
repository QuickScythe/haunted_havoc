package me.quickscythe.shadowutils.extras.entity;

import me.quickscythe.shadowcore.utils.entity.CustomEntityRegistry;
import me.quickscythe.shadowutils.utils.Utils;
import net.minecraft.world.entity.Mob;

public enum HauntedEntities {
    CUSTOM_ZOMBIE("customzombie", CustomZombie.class);

    String key;
    Class<? extends Mob> clazz;

    HauntedEntities(String key, Class<? extends Mob> clazz){
        this.key = key;
        this.clazz = clazz;
    }

    public void register(CustomEntityRegistry cer){
        cer.register(key, clazz);
    }
}
