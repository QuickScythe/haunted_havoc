package me.quickscythe.shadowutils.extras.entity;

import me.quickscythe.shadowcore.utils.entity.CustomMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class CustomZombie extends Zombie implements CustomMob {
    public CustomZombie(Level world) {
        super(world);
    }
}
