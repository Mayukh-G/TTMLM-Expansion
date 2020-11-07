package com.example.ttmlm.entity.ai.goal;

import com.example.ttmlm.entity.changed.HardEnderman;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import java.util.EnumSet;
import java.util.Random;

public class EndermanGoals {

    //Stare Goal --------------------------------------------------------------
    public static class StareGoal extends Goal {
        private final HardEnderman enderman;
        private LivingEntity targetPlayer;

        public StareGoal(HardEnderman endermanIn) {
            this.enderman = endermanIn;
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            this.targetPlayer = this.enderman.getAttackTarget();
            if (!(this.targetPlayer instanceof PlayerEntity)) {
                return false;
            } else {
                double d0 = this.targetPlayer.getDistanceSq(this.enderman);
                return !(d0 > 256.0D) && this.enderman.shouldBeAttackingPlayer((PlayerEntity) this.targetPlayer);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.enderman.getNavigator().clearPath();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.enderman.getLookController().setLookPosition(this.targetPlayer.getPosX(), this.targetPlayer.getPosYEye(), this.targetPlayer.getPosZ());
        }
    }

    //Place Block Goal --------------------------------------------------------------

    public static class PlaceBlockGoal extends Goal {
        private final HardEnderman enderman;

        public PlaceBlockGoal(HardEnderman p_i45843_1_) {
            this.enderman = p_i45843_1_;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.enderman.getHeldBlockState() == null) {
                return false;
            }else return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.enderman.world, this.enderman);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            Random random = this.enderman.getRNG();
            IWorld iworld = this.enderman.world;
            Vec3d vec3d = this.enderman.getLook(1.0F);
            int i = MathHelper.floor((-1 * vec3d.x) + this.enderman.getPosX() - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.enderman.getPosY() + random.nextDouble() * 2.0D);
            int k = MathHelper.floor((-1 * vec3d.z) + this.enderman.getPosZ() - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            BlockState blockstate = iworld.getBlockState(blockpos);
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate1 = iworld.getBlockState(blockpos1);
            BlockState blockstate2 = this.enderman.getHeldBlockState();
            if (blockstate2 != null && this.func_220836_a(iworld, blockpos, blockstate2, blockstate, blockstate1, blockpos1)  && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(enderman, new net.minecraftforge.common.util.BlockSnapshot(iworld, blockpos, blockstate1), net.minecraft.util.Direction.UP)) {
                iworld.setBlockState(blockpos, blockstate2, 3);
                this.enderman.setHeldBlockState((BlockState)null);
            }

        }

        private boolean func_220836_a(IWorldReader p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_3_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
            return p_220836_4_.isAir(p_220836_1_, p_220836_2_) && !p_220836_5_.isAir(p_220836_1_, p_220836_6_) && p_220836_5_.isCollisionShapeOpaque(p_220836_1_, p_220836_6_) && p_220836_3_.isValidPosition(p_220836_1_, p_220836_2_);
        }
    }

    // Take Block Goal  --------------------------------------------------------------

    public static class TakeBlockGoal extends Goal {
        private final HardEnderman enderman;
        private BlockPos takeBlock;

        public TakeBlockGoal(HardEnderman endermanIn) {
            this.enderman = endermanIn;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            if (this.enderman.getHeldBlockState() != null) {
                return false;
            } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.enderman.world, this.enderman)) {
                return false;
            } else {
                if(this.enderman.isAggressive()){
                    World world = this.enderman.world;
                    BlockPos poshead = this.enderman.getPosition().up().up();
                    BlockPos poschest = this.enderman.getPosition().up();
                    //Check head area
                    if(world.getBlockState(poshead.north()).isCollisionShapeOpaque(world, poshead.north())){
                        this.takeBlock = poshead.north();
                        return true;
                    }
                    else if(world.getBlockState(poshead.east()).isCollisionShapeOpaque(world, poshead.east())){
                        this.takeBlock = poshead.east();
                        return true;
                    }
                    else if(world.getBlockState(poshead.south()).isCollisionShapeOpaque(world, poshead.south())){
                        this.takeBlock = poshead.south();
                        return true;
                    }
                    else if(world.getBlockState(poshead.west()).isCollisionShapeOpaque(world, poshead.west())){
                        this.takeBlock = poshead.west();
                        return true;
                    }
                    //Chest area
                    if(world.getBlockState(poschest.north()).isCollisionShapeOpaque(world, poschest.north())){
                        this.takeBlock = poschest.north();
                        return true;
                    }
                    else if(world.getBlockState(poschest.east()).isCollisionShapeOpaque(world, poschest.east())){
                        this.takeBlock = poschest.east();
                        return true;
                    }
                    else if(world.getBlockState(poschest.south()).isCollisionShapeOpaque(world, poschest.south())){
                        this.takeBlock = poschest.south();
                        return true;
                    }
                    else if(world.getBlockState(poschest.west()).isCollisionShapeOpaque(world, poschest.west())){
                        this.takeBlock = poschest.west();
                        return true;
                    }
                }
                return false;
            }

        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            World world = this.enderman.world;
            BlockState state = world.getBlockState(this.takeBlock);
            if (!(state.getBlock() instanceof ContainerBlock) && !(state.getBlock().isIn(BlockTags.PORTALS)) && !(state.getBlock().isIn(Tags.Blocks.END_STONES)) && state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.OBSIDIAN && state.getBlock() != Blocks.END_PORTAL_FRAME) {
                this.enderman.setHeldBlockState(state);
                world.removeBlock(this.takeBlock, false);
            }
        }
    }

    //Find player Goal ------------------------------------

    public static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        private final HardEnderman enderman;
        /** The player */
        private PlayerEntity player;
        private int aggroTime;
        private int teleportTime;
        private final EntityPredicate field_220791_m;
        private final EntityPredicate field_220792_n = (new EntityPredicate()).setLineOfSiteRequired();

        public FindPlayerGoal(HardEnderman endermanIn) {
            super(endermanIn, PlayerEntity.class, false);
            this.enderman = endermanIn;
            this.field_220791_m = (new EntityPredicate()).setDistance(this.getTargetDistance()).setCustomPredicate((p_220790_1_) -> endermanIn.shouldBeAttackingPlayer((PlayerEntity)p_220790_1_));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            this.player = this.enderman.world.getClosestPlayer(this.field_220791_m, this.enderman);
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
            this.enderman.func_226538_eu_();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.player = null;
            super.resetTask();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            if (this.player != null) {
                if (!this.enderman.shouldBeAttackingPlayer(this.player)) {
                    return false;
                } else {
                    this.enderman.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.nearestTarget != null && this.field_220792_n.canTarget(this.enderman, this.nearestTarget) || super.shouldContinueExecuting();
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.nearestTarget = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            } else {
                if (this.nearestTarget != null && !this.enderman.isPassenger()) {
                    if (this.enderman.shouldBeAttackingPlayer((PlayerEntity)this.nearestTarget)) {
                        if (this.nearestTarget.getDistanceSq(this.enderman) < 16.0D) {
                            this.enderman.teleportRandomly();
                        }

                        this.teleportTime = 0;
                    } else if (this.nearestTarget.getDistanceSq(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportAtEntity(this.nearestTarget)) {
                        this.teleportTime = 0;
                    }
                }

                super.tick();
            }

        }
    }
}
