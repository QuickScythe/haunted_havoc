package me.quickscythe.shadowutils.extras.entity.pathfinding;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;

import javax.annotation.Nullable;

public class CustomSwellGoal extends SwellGoal {
    private final Creeper creeper;
    @Nullable
    private LivingEntity target;

    public CustomSwellGoal(Creeper creeper) {
        super(creeper);
        this.creeper = creeper;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.creeper.getTarget();
        return this.creeper.getSwellDir() > 0 || livingEntity != null && this.creeper.distanceToSqr(livingEntity) < 1;
    }

    // Paper start - Fix MC-179072
    @Override
    public boolean canContinueToUse() {
        return !net.minecraft.world.entity.EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(this.creeper.getTarget()) && canUse();
    }
    // Paper end

    @Override
    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.creeper.getTarget();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.distanceToSqr(this.target) > 49.0/2) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.getSensing().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}
