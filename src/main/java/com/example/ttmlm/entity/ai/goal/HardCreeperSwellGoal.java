package com.example.ttmlm.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.CreeperEntity;

import java.util.EnumSet;


public class HardCreeperSwellGoal extends Goal {
    private final CreeperEntity swellingHardCreeper;
    private LivingEntity creeperAttackTarget;

    public HardCreeperSwellGoal(CreeperEntity entitycreeperIn) {
        this.swellingHardCreeper = entitycreeperIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.swellingHardCreeper.getTarget();
        return this.swellingHardCreeper.getSwellDir() > 0 || livingentity != null && this.swellingHardCreeper.distanceToSqr(livingentity) < 20.0D;
    }

    @Override
    public void start() {
        this.swellingHardCreeper.getNavigation().stop();
        this.creeperAttackTarget = this.swellingHardCreeper.getTarget();
    }

    @Override
    public void stop() {
        this.creeperAttackTarget = null;
    }

    @Override
    public void tick() {
        if (this.creeperAttackTarget == null) {
            this.swellingHardCreeper.setSwellDir(-1);
        } else if (this.swellingHardCreeper.distanceToSqr(this.creeperAttackTarget) > 45.0D) {
            this.swellingHardCreeper.setSwellDir(-1);
        } else if (!this.swellingHardCreeper.getSensing().canSee(this.creeperAttackTarget)) {
            this.swellingHardCreeper.setSwellDir(-1);
        } else {
            this.swellingHardCreeper.setSwellDir(1);
        }
    }
}
