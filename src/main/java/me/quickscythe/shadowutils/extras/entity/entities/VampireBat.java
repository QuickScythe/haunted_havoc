package me.quickscythe.shadowutils.extras.entity.entities;

import me.quickscythe.shadowutils.extras.entity.pathfinding.CustomAttackGoal;
import me.quickscythe.shadowutils.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftBat;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class VampireBat extends Bat {

    public VampireBat(Level world) {
        super(EntityType.BAT, world);

        ((CraftBat) getBukkitEntity()).registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        ((CraftBat) getBukkitEntity()).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2.5);

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }


    @Override
    protected void registerGoals() {
//        super.registerGoals();
//        goalSelector.removeAllGoals((goal)-> true);
//        this.goToKnownFlowerGoal = new Bee.BeeGoToKnownFlowerGoal();
        this.goalSelector.addGoal(1, new BatAttackGoal(this, 1.399999976158142D, true));
//        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
//        this.beePollinateGoal = new Bee.BeePollinateGoal();
//        this.goalSelector.addGoal(4, this.beePollinateGoal);
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 50));
//        this.goalSelector.addGoal(8, new BatWanderGoal());
        this.goalSelector.addGoal(2, new FollowMobGoal(this, 1.399999976158142D, 0, 50));
//        this.goalSelector.addGoal(3, new FloatGoal(this));
//        this.targetSelector.addGoal(0, (new BatHurtByOtherGoal(this)).setAlertOthers(new Class[0]));
//        this.targetSelector.addGoal(2, new BatBecomeAngryTargetGoal(this));
//        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
//        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }

    @Override
    public void tick() {
        super.tick();
        for (Entity entity : getBukkitEntity().getNearbyEntities(50, 50, 50)) {
            if (entity instanceof org.bukkit.entity.Player player) {
                setTarget((LivingEntity) ((CraftEntity) entity).getHandle(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER, false);
//                ((CraftBee) getBukkitEntity()).setTarget(player);
            }
        }
    }


    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        setResting(false);
        for (Entity entity : getBukkitEntity().getNearbyEntities(10, 10, 10)) {
            if (entity instanceof Player player) {
                setTarget(player);
            }
        }
        if (getTarget() != null && this.distanceTo(getTarget()) <= 1) {
            Utils.getLogger().log("TEST");
            this.doHurtTarget(getTarget());
        }

    }

    private static class BatBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {

        BatBecomeAngryTargetGoal(VampireBat bat) {
            // Objects.requireNonNull(entitybee); // CraftBukkit - decompile error
            super(bat, Player.class, 10, true, false, (entity) -> true);
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }

        private boolean beeCanTarget() {
            return true;
        }
    }

    private class BatAttackGoal extends CustomAttackGoal {

        BatAttackGoal(final Mob mob, final double speed, final boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean canUse() {
//            if (getTarget() != null && mob.distanceTo(getTarget()) <= 1){
//                Utils.getLogger().log("TEST");
//                mob.doHurtTarget(getTarget());
//            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return true;
        }
    }

    private class BatWanderGoal extends Goal {

        private static final int WANDER_THRESHOLD = 22;

        BatWanderGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return VampireBat.this.navigation.isDone() && VampireBat.this.random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return VampireBat.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            Vec3 vec3d = this.findPos();

            if (vec3d != null) {
                VampireBat.this.navigation.moveTo(VampireBat.this.navigation.createPath(BlockPos.containing(vec3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vec3 findPos() {
            return VampireBat.this.getViewVector(0.0F);
//            Vec3 vec3d = VampireBat.this.getViewVector(0.0F);
//            boolean flag = true;
//            Vec3 vec3d2 = HoverRandomPos.getPos(VampireBat.this, 8, 7, vec3d.x, vec3d.z, 1.5707964F, 3, 1);
//            return vec3d2 != null ? vec3d2 : AirAndWaterRandomPos.getPos(VampireBat.this, 8, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866D);
        }
    }

    private class BatHurtByOtherGoal extends HurtByTargetGoal {

        BatHurtByOtherGoal(final Bee entitybee) {
            super(entitybee);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        protected void alertOther(Mob mob, LivingEntity target) {

        }
    }
}
