package me.quickscythe.shadowutils.extras.entity.entities;

import me.quickscythe.shadowutils.extras.entity.pathfinding.CustomSwellGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;

public class BabyCreeper extends Creeper {
    public BabyCreeper(Level world) {
        super(EntityType.CREEPER, world);
        ((CraftLivingEntity) getBukkitEntity()).registerAttribute(Attribute.GENERIC_SCALE);
        ((CraftLivingEntity) getBukkitEntity()).registerAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        ((CraftLivingEntity) getBukkitEntity()).getAttribute(Attribute.GENERIC_SCALE).setBaseValue(0.5D);
        ((CraftLivingEntity) getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);

//        getAttributes().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CustomSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }

    @Override
    public void explodeCreeper() {
        this.explosionRadius = 1;
        super.explodeCreeper();
    }
}
