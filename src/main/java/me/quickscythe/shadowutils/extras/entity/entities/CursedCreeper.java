package me.quickscythe.shadowutils.extras.entity.entities;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.entity.CraftCreeper;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.Iterator;

import static net.kyori.adventure.text.Component.text;

public class CursedCreeper extends Creeper {
    public CursedCreeper(Level world) {
        super(EntityType.CREEPER, world);
        setCustomName(net.minecraft.network.chat.Component.literal("Cursed Creeper").withColor(NamedTextColor.GOLD.value()));


    }

    @Override
    public void explodeCreeper() {
        getBukkitEntity().remove();
//        this.discard(EntityRemoveEvent.Cause.DEATH);
//        ParticleOptions opt = ParticleTypes.ANGRY_VILLAGER;
        Particle.DustOptions opt = new Particle.DustOptions(Color.BLACK, 3);
        for(int i=0;i!=100;i++){
            getBukkitEntity().getWorld().spawnParticle(Particle.DUST, getBukkitEntity().getLocation().clone().add(0,1,0), 1, 1.5,1.5,1.5,opt);
//            this.level().addParticle(opt, position().x(),position().y+2D,position().z,0,0,0);
        }
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
