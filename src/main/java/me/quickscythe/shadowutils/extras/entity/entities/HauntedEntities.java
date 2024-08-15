package me.quickscythe.shadowutils.extras.entity.entities;

import me.quickscythe.shadowcore.utils.entity.CustomEntityRegistry;
import net.minecraft.world.entity.Entity;

public class HauntedEntities {

    final static Class<? extends Entity> CURSED_CREEPER = CursedCreeper.class;
    final static Class<? extends Entity> VAMPIRE_BAT = VampireBat.class;
    final static Class<? extends Entity> HAUNTED_ARMOR = HauntedArmor.class;

    public static void init(CustomEntityRegistry cer) {
       cer.register("cursed_creeper", CURSED_CREEPER);
       cer.register("vampire_bat", VAMPIRE_BAT);
       cer.register("haunted_armor", HAUNTED_ARMOR);
    }


}
