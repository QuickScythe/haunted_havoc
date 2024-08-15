package me.quickscythe.shadowutils.extras.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Iterator;

import static net.kyori.adventure.text.Component.text;

public class CursedCreeper extends Creeper implements CustomEntity {
    public CursedCreeper(Level world) {
        super(EntityType.CREEPER, world);
        setCustomName(net.minecraft.network.chat.Component.literal("Cursed Creeper").withColor(NamedTextColor.GOLD.value()));


    }

    @Override
    public void explodeCreeper() {
        getBukkitEntity().remove();
//        this.discard(EntityRemoveEvent.Cause.DEATH);
        ((CraftCreeper) getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 255, 255));
        spawnLingeringCloud();
    }
    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.getActiveEffects();

        if (!collection.isEmpty() && !this.level().paperConfig().entities.behavior.disableCreeperLingeringEffect) { // Paper - Option to disable creeper lingering effect
            AreaEffectCloud entityareaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());

            entityareaeffectcloud.setOwner(this); // CraftBukkit
            entityareaeffectcloud.setRadius(2.5F);
            entityareaeffectcloud.setRadiusOnUse(-0.5F);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
            entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                MobEffectInstance mobeffect = (MobEffectInstance) iterator.next();

                entityareaeffectcloud.addEffect(new MobEffectInstance(mobeffect));
            }

            this.level().addFreshEntity(entityareaeffectcloud, CreatureSpawnEvent.SpawnReason.EXPLOSION); // CraftBukkit
        }

    }
}
