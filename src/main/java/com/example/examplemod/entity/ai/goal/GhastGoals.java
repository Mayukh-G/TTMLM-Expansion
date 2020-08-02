package com.example.examplemod.entity.ai.goal;

import com.example.examplemod.entity.changed.HardGhast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.attackTimer = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.parentEntity.setAttacking(false);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            LivingEntity livingentity = this.parentEntity.getAttackTarget();
            if (livingentity.getDistanceSq(this.parentEntity) < 4096.0D && this.parentEntity.canEntityBeSeen(livingentity)) {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playEvent((PlayerEntity) null, 1015, new BlockPos(this.parentEntity), 0);
                }

                if (this.attackTimer == 20) {
                    Vec3d vec3d = this.parentEntity.getLook(1.0F);
                    double d2 = livingentity.getPosX() - (this.parentEntity.getPosX() + vec3d.x * 4.0D);
                    double d3 = livingentity.getPosYHeight(0.5D) - (0.5D + this.parentEntity.getPosYHeight(0.5D));
                    double d4 = livingentity.getPosZ() - (this.parentEntity.getPosZ() + vec3d.z * 4.0D);
                    double fx = this.parentEntity.getPosX() + vec3d.x * 4.0D;
                    double fy = this.parentEntity.getPosYHeight(1.0D) + 1.0D;  //Will spawn On top of entity
                    double fz = this.parentEntity.getPosZ() + vec3d.z * 4.0D;
                    int xchange = 0;
                    int zchange = 0;
                    world.playEvent((PlayerEntity) null, 1016, new BlockPos(this.parentEntity), 0);
                    //logic for head rotation
                    if(this.parentEntity.rotationYawHead >= 315 || this.parentEntity.rotationYawHead < 45){ //change x
                        xchange = 5;
                    }else if(this.parentEntity.rotationYawHead >= 45 & this.parentEntity.rotationYawHead < 135){ //change z
                        zchange = 5;
                    }else if(this.parentEntity.rotationYawHead >= 135 & this.parentEntity.rotationYawHead < 225){ //change x
                        xchange = 5;
                    }else if(this.parentEntity.rotationYawHead >= 255 & this.parentEntity.rotationYawHead < 315){ //change z
                        zchange = 5;
                    }
                    //Fireball one middle top
                    FireballEntity fireballentity1 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity1.explosionPower = this.parentEntity.getFireballStrength();
                    fireballentity1.setPosition(fx, fy, fz);
                    //FireBall two bottom 1
                    FireballEntity fireballentity2 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity2.explosionPower = this.parentEntity.getFireballStrength();
                    fireballentity2.setPosition(fx - xchange, this.parentEntity.getPosY() - 0.5D, fz - zchange);
                    //FireBall three bottom 2
                    FireballEntity fireballentity3 = new FireballEntity(world, this.parentEntity, d2 * 2, d3 * 2, d4 * 2);
                    fireballentity3.explosionPower = this.parentEntity.getFireballStrength();
                    fireballentity3.setPosition(fx + xchange, this.parentEntity.getPosY() - 0.5D, fz + zchange);


                    world.addEntity(fireballentity3);
                    world.addEntity(fireballentity2);
                    world.addEntity(fireballentity1);
                    this.attackTimer = -40;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }

            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }

    //Random Fly Goal -------------------------------

    public static class RandomFlyGoal extends Goal {
        private final HardGhast parentEntity;

        public RandomFlyGoal(HardGhast ghast) {
            this.parentEntity = ghast;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            MovementController movementcontroller = this.parentEntity.getMoveHelper();
            if (!movementcontroller.isUpdating()) {
                return true;
            } else {
                double d0 = movementcontroller.getX() - this.parentEntity.getPosX();
                double d1 = movementcontroller.getY() - this.parentEntity.getPosY();
                double d2 = movementcontroller.getZ() - this.parentEntity.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.getPosX() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.getPosY() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.getPosZ() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

    //Look Around Goal -------------------------------

    public static class LookAroundGoal extends Goal {
        private final HardGhast parentEntity;

        public LookAroundGoal(HardGhast ghast) {
            this.parentEntity = ghast;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return true;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.parentEntity.getAttackTarget() == null) {
                Vec3d vec3d = this.parentEntity.getMotion();
                this.parentEntity.rotationYaw = -((float) MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float) Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            } else {
                LivingEntity livingentity = this.parentEntity.getAttackTarget();
                double d0 = 64.0D;
                if (livingentity.getDistanceSq(this.parentEntity) < 4096.0D) {
                    double d1 = livingentity.getPosX() - this.parentEntity.getPosX();
                    double d2 = livingentity.getPosZ() - this.parentEntity.getPosZ();
                    this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }

        }
    }

}