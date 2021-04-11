package com.example.ttmlm.entity.ai.goal;

import com.example.ttmlm.entity.changed.HardEnderman;
import net.minecraft.block.Block;
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
import net.minecraft.util.math.vector.Vector3d;
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
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            this.targetPlayer = this.enderman.getTarget();
            if (!(this.targetPlayer instanceof PlayerEntity)) {
                return false;
            } else {
                double d0 = this.targetPlayer.distanceToSqr(this.enderman);
                return !(d0 > 256.0D) && this.enderman.isLookingAtMe((PlayerEntity) this.targetPlayer);
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.enderman.getNavigation().stop();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.enderman.getLookControl().setLookAt(this.targetPlayer.getX(), this.targetPlayer.getEyeY(), this.targetPlayer.getZ());
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
        public boolean canUse() {
            if (this.enderman.getCarriedBlock() == null) {
                return false;
            }else return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.enderman.level, this.enderman);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            Random random = this.enderman.getRandom();
            World iworld = this.enderman.level;
            Vector3d vec3d = this.enderman.getViewVector(1.0F);
            int i = MathHelper.floor((-1 * vec3d.x) + this.enderman.getX() - 1.0D + random.nextDouble() * 2.0D);
            int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 2.0D);
            int k = MathHelper.floor((-1 * vec3d.z) + this.enderman.getZ() - 1.0D + random.nextDouble() * 2.0D);
            BlockPos blockpos = new BlockPos(i, j, k);
            BlockState blockstate = iworld.getBlockState(blockpos);
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate1 = iworld.getBlockState(blockpos1);
            BlockState blockstate2 = this.enderman.getCarriedBlock();
            if (blockstate2 != null) {
                blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.enderman.level, blockpos);
                if (this.canPlaceBlock(iworld, blockpos, blockstate2, blockstate, blockstate1, blockpos1) && !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(enderman, net.minecraftforge.common.util.BlockSnapshot.create(iworld.dimension(), iworld, blockpos1), net.minecraft.util.Direction.UP)) {
                    iworld.setBlock(blockpos, blockstate2, 3);
                    this.enderman.setCarriedBlock((BlockState) null);
                }
            }

        }

        private boolean canPlaceBlock(World p_220836_1_, BlockPos p_220836_2_, BlockState p_220836_3_, BlockState p_220836_4_, BlockState p_220836_5_, BlockPos p_220836_6_) {
            return p_220836_4_.is(Blocks.AIR) && !p_220836_5_.is(Blocks.AIR) && !p_220836_5_.is(Blocks.BEDROCK) && !p_220836_5_.is(net.minecraftforge.common.Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST) && p_220836_5_.isCollisionShapeFullBlock(p_220836_1_, p_220836_6_) && p_220836_3_.canSurvive(p_220836_1_, p_220836_2_) && p_220836_1_.getEntities(this.enderman, AxisAlignedBB.unitCubeFromLowerCorner(Vector3d.atLowerCornerOf(p_220836_2_))).isEmpty();
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
        public boolean canUse() {
            if (this.enderman.getCarriedBlock() != null) {
                return false;
            } else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.enderman.level, this.enderman)) {
                return false;
            } else {
                if(this.enderman.isAggressive()){
                    World world = this.enderman.level;
                    BlockPos poshead = this.enderman.blockPosition().above(2);
                    BlockPos poschest = this.enderman.blockPosition().above();
                    //Check head area
                    if(world.getBlockState(poshead.north()).isCollisionShapeFullBlock(world, poshead.north())){
                        this.takeBlock = poshead.north();
                        return true;
                    }
                    else if(world.getBlockState(poshead.east()).isCollisionShapeFullBlock(world, poshead.east())){
                        this.takeBlock = poshead.east();
                        return true;
                    }
                    else if(world.getBlockState(poshead.south()).isCollisionShapeFullBlock(world, poshead.south())){
                        this.takeBlock = poshead.south();
                        return true;
                    }
                    else if(world.getBlockState(poshead.west()).isCollisionShapeFullBlock(world, poshead.west())){
                        this.takeBlock = poshead.west();
                        return true;
                    }
                    //Chest area
                    if(world.getBlockState(poschest.north()).isCollisionShapeFullBlock(world, poschest.north())){
                        this.takeBlock = poschest.north();
                        return true;
                    }
                    else if(world.getBlockState(poschest.east()).isCollisionShapeFullBlock(world, poschest.east())){
                        this.takeBlock = poschest.east();
                        return true;
                    }
                    else if(world.getBlockState(poschest.south()).isCollisionShapeFullBlock(world, poschest.south())){
                        this.takeBlock = poschest.south();
                        return true;
                    }
                    else if(world.getBlockState(poschest.west()).isCollisionShapeFullBlock(world, poschest.west())){
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
            World world = this.enderman.level;
            BlockState state = world.getBlockState(this.takeBlock);
            if (!(state.getBlock() instanceof ContainerBlock) && !(state.getBlock().is(BlockTags.PORTALS)) && !(state.getBlock().is(Tags.Blocks.END_STONES)) && state.getBlock() != Blocks.BEDROCK && state.getBlock() != Blocks.OBSIDIAN && state.getBlock() != Blocks.END_PORTAL_FRAME) {
                this.enderman.setCarriedBlock(state);
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
        private final EntityPredicate field_220792_n = (new EntityPredicate()).allowUnseeable();

        public FindPlayerGoal(HardEnderman endermanIn) {
            super(endermanIn, PlayerEntity.class, false);
            this.enderman = endermanIn;
            this.field_220791_m = (new EntityPredicate()).range(this.getFollowDistance()).selector((p_220790_1_) -> endermanIn.isLookingAtMe((PlayerEntity)p_220790_1_));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            this.player = this.enderman.level.getNearestPlayer(this.field_220791_m, this.enderman);
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.aggroTime = 5;
            this.teleportTime = 0;
            this.enderman.setBeingStaredAt();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.player = null;
            super.stop();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            if (this.player != null) {
                if (!this.enderman.isLookingAtMe(this.player)) {
                    return false;
                } else {
                    this.enderman.lookAt(this.player, 10.0F, 10.0F);
                    return true;
                }
            } else {
                return this.target != null && this.field_220792_n.test(this.enderman, this.target) || super.canContinueToUse();
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.target = this.player;
                    this.player = null;
                    super.start();
                }
            } else {
                if (this.target != null && !this.enderman.isPassenger()) {
                    if (this.enderman.isLookingAtMe((PlayerEntity)this.target)) {
                        if (this.target.distanceToSqr(this.enderman) < 16.0D) {
                            this.enderman.teleport();
                        }

                        this.teleportTime = 0;
                    } else if (this.target.distanceToSqr(this.enderman) > 256.0D && this.teleportTime++ >= 30 && this.enderman.teleportAtEntity(this.target)) {
                        this.teleportTime = 0;
                    }
                }

                super.tick();
            }

        }
    }
}
