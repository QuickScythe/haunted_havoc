package me.quickscythe.shadowutils.extras.entity;

import me.quickscythe.shadowcore.utils.chat.Logger;
import me.quickscythe.shadowcore.utils.entity.CustomEntityRegistry;
import me.quickscythe.shadowutils.utils.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class HauntedEntities {

    final static Class<? extends Entity> CURSED_CREEPER = CursedCreeper.class;
    final static Class<? extends Entity> VAMPIRE_BAT = VampireBat.class;
    final static Class<? extends Entity> HAUNTED_ARMOR = HauntedArmor.class;

    public static void init(CustomEntityRegistry cer) {
       cer.register("CURSED_CREEPER", CURSED_CREEPER);
       cer.register("VAMPIRE_BAT", VAMPIRE_BAT);
       cer.register("HAUNTED_ARMOR", HAUNTED_ARMOR);
    }


}
