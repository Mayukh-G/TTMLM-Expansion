package com.example.examplemod.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.CreeperEntity;

import java.util.EnumSet;


public class HardCreeperSwellGoal extends Goal {
    private final CreeperEntity swellingHardCreeper;
    private LivingEntity creeperAttackTarget;

    public HardCreeperSwellGoal(CreeperEntity entitycreeperIn) {
        this.swellingHardCreeper = entitycreeperIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        LivingEntity livingentity = this.swellingHardCreeper.getAttackTarget();
        return this.swellingHardCreeper.getCreeperState() > 0 || livingentity != null && this.swellingHardCreeper.getDistanceSq(livingentity) < 20.0D;
    }

    @Override
    public void startExecuting() {
        this.swellingHardCreeper.getNavigator().clearPath();
        this.creeperAttackTarget = this.swellingHardCreeper.getAttackTarget();
    }

    @Override
    public void resetTask() {
        this.creeperAttackTarget = null;
    }

    @Override
    public void tick() {
        if (this.creeperAttackTarget == null) {
            this.swellingHardCreeper.setCreeperState(-1);
        } else if (this.swellingHardCreeper.getDistanceSq(this.creeperAttackTarget) > 50.0D) {
            this.swellingHardCreeper.setCreeperState(-1);
        } else if (!this.swellingHardCreeper.getEntitySenses().canSee(this.creeperAttackTarget)) {
            this.swellingHardCreeper.setCreeperState(-1);
        } else {
            this.swellingHardCreeper.setCreeperState(1);
        }
    }
}
