package com.example.ttmlm.entity.ai.goal;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.HardGhast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class GhastGoals {
    // Fire ball attack ----------------------------------
    public static class FireballAttackGoal extends Goal {
        private final HardGhast parentEntity;
        public int attackTimer;

        public FireballAttackGoal(HardGhast ghast) {
            this.parentEntity = ghast;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.parentEntity.getTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.attackTimer = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.parentEntity.setAggressive(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.parentEntity.getTarget();
            if (livingentity.distanceToSqr(this.parentEntity) < 4096.0D && this.parentEntity.canSee(livingentity)) {
                World world = this.parentEntity.level;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.levelEvent((PlayerEntity) null, 1015, this.parentEntity.blockPosition(), 0);
                }

                if (this.attackTimer == 20) {
                    Vector3d vec3d = this.parentEntity.getViewVector(1.0F);
                    double d2 = livingentity.getX() - (this.parentEntity.getX() + vec3d.x * 4.0D);
                    double d3 = livingentity.getY(0.5D) - (0.5D + this.parentEntity.getY(0.5D));
                    double d4 = livingentity.getZ() - (this.parentEntity.getZ() + vec3d.z * 4.0D);
                    double fx = this.parentEntity.getX() + vec3d.x * 4.0D;
                    double fy = this.parentEntity.getY(1.0D) + 1.0D;  //Will spawn On top of entity
                    double fz = this.parentEntity.getZ() + vec3d.z * 4.0D;
                    int xchange = 0;
                    int zchange = 0;
                    world.levelEvent((PlayerEntity) null, 1016, this.parentEntity.blockPosition(), 0);
                    //logic for head rotation
                    if (TTMLM.isDevBuild()) TTMLM.LOGGER.debug(-this.parentEntity.yHeadRot);
                    float rot = Math.abs(this.parentEntity.yHeadRot);
                    if(rot >= 315 || rot < 45){ //change x
                        xchange = 5;
                    }else if(rot >= 45 & rot < 135){ //change z
                        zchange = 5;
                    }else if(rot >= 135 & rot < 225){ //change x
                        xchange = 5;
                    }else if(rot >= 255 & rot < 315){ //change z
                        zchange = 5;
                    }
                    //Fireball one middle top
                    FireballEntity fireballentity1 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity1.explosionPower = this.parentEntity.explodePow();
                    fireballentity1.setPos(fx, fy, fz);
                    //FireBall two bottom 1
                    FireballEntity fireballentity2 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity2.explosionPower = this.parentEntity.explodePow();
                    fireballentity2.setPos(fx - xchange, this.parentEntity.getY() - 0.5D, fz - zchange);
                    //FireBall three bottom 2
                    FireballEntity fireballentity3 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity3.explosionPower = this.parentEntity.explodePow();
                    fireballentity3.setPos(fx + xchange, this.parentEntity.getY() - 0.5D, fz + zchange);


                    world.addFreshEntity(fireballentity3);
                    world.addFreshEntity(fireballentity2);
                    world.addFreshEntity(fireballentity1);
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }

            this.parentEntity.setAggressive(this.attackTimer > 10);
        }
    }

    //Random Fly Goal -------------------------------

    public static class RandomFlyGoal extends Goal {
        private final HardGhast parentEntity;

        public RandomFlyGoal(HardGhast ghast) {
            this.parentEntity = ghast;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            MovementController movementcontroller = this.parentEntity.getMoveControl();
            if (!movementcontroller.hasWanted()) {
                return true;
            } else {
                double d0 = movementcontroller.getWantedX() - this.parentEntity.getX();
                double d1 = movementcontroller.getWantedY() - this.parentEntity.getY();
                double d2 = movementcontroller.getWantedZ() - this.parentEntity.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Random random = this.parentEntity.getRandom();
            double d0 = this.parentEntity.getX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.getY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.getZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
        }
    }

    //Look Around Goal -------------------------------

    public static class LookAroundGoal extends Goal {
        private final HardGhast parentEntity;

        public LookAroundGoal(HardGhast ghast) {
            this.parentEntity = ghast;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.parentEntity.getTarget() == null) {
                Vector3d movementVec = this.parentEntity.getDeltaMovement();
                this.parentEntity.yRot = -((float)MathHelper.atan2(movementVec.x, movementVec.z)) * 57.295776F;
                this.parentEntity.yBodyRot = this.parentEntity.yRot;
            } else {
                LivingEntity target = this.parentEntity.getTarget();
                double unused_64 = 64.0D;
                if (target.distanceToSqr(this.parentEntity) < 4096.0D) {
                    double d0 = target.getX() - this.parentEntity.getX();
                    double d1 = target.getZ() - this.parentEntity.getZ();
                    this.parentEntity.yRot = -((float)MathHelper.atan2(d0, d1)) * 57.295776F;
                    this.parentEntity.yBodyRot = this.parentEntity.yRot;
                }
            }

        }
    }

}