package com.example.ttmlm.item.tools;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariants;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.IProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class IngotVariantHoes extends HoeItem {
    private IngotVariants variant;

    public IngotVariantHoes(IItemTier tier, float attackSpeedIn, IngotVariants variant, Properties builder) {
        super(tier, attackSpeedIn, builder.group(TTMLM.ITEM_GROUP_TOOL));
        this.variant = variant;
    }
    // Since onItemUse does not work on fluids, this had to be done separately
    // Finds Water and replaces it and other water in an area around it with ice
    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote && this.variant == IngotVariants.FREEZING_ALLOY) {
            // TODO : Vector still not 100% accurate Something is not right.
            double x = playerIn.getLookVec().x;
            double y = playerIn.getLookVec().y;
            double z = playerIn.getLookVec().z; // 23.3, 62, -125.5
            int checked = 0;
            Vec3d start = playerIn.getPositionVec().add(0,1.5,0);
//            TTMLM.LOGGER.debug(start);
//            TTMLM.LOGGER.debug(playerIn.getLookVec());
            BlockPos checkingPos = new BlockPos(start);
            boolean found = false;
            for (int i = 1; checked < 4 && !found ; i++) {
                double tempX = start.x + x * i;
                double tempY = start.y + y * i;
                double tempZ = start.z + z * i;
                BlockPos temp = new BlockPos(tempX, tempY, tempZ);
                if (temp.getX() != checkingPos.getX() || temp.getY() != checkingPos.getY() || temp.getZ() != checkingPos.getZ()) {
                    checkingPos = temp;
                    IFluidState state = worldIn.getFluidState(checkingPos);
                    if (state == Fluids.WATER.getStillFluidState(false) || state == Fluids.WATER.getStillFluidState(true)
                    || state == Fluids.LAVA.getStillFluidState(false) || state == Fluids.LAVA.getStillFluidState(true) ) {
                        found = true;
                        IFluidState checkStateNoFall = (state.getFluid() == Fluids.LAVA) ? Fluids.LAVA.getStillFluidState(false) : Fluids.WATER.getStillFluidState(false);
                        IFluidState checkStateFall = (state.getFluid() == Fluids.LAVA) ? Fluids.LAVA.getStillFluidState(true) : Fluids.WATER.getStillFluidState(true);
                        BlockState replaceState = (state.getFluid() == Fluids.LAVA) ? Blocks.OBSIDIAN.getDefaultState() : Blocks.ICE.getDefaultState();
                        SoundEvent sound = (state.getFluid() == Fluids.LAVA) ? SoundEvents.BLOCK_LAVA_EXTINGUISH : SoundEvents.BLOCK_GLASS_BREAK;
                        worldIn.setBlockState(checkingPos, replaceState);
                        BlockPos[] positions = new BlockPos[]{
                                checkingPos.north().north(), checkingPos.north(), checkingPos.north().east(),
                                checkingPos.north().west(), checkingPos.south().south(), checkingPos.south(),
                                checkingPos.south().west(), checkingPos.south().east(), checkingPos.west().west(),
                                checkingPos.west(), checkingPos.east().east(), checkingPos.east()

                        };
                        worldIn.playSound(null, checkingPos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        for (BlockPos position : positions) {
                            state = worldIn.getFluidState(position);
                            if (state == checkStateFall || state == checkStateNoFall) {
                                worldIn.setBlockState(position, replaceState);
                            }
                        }
                    }
                    checked++;
                }
            }// for end
            if(found){
                playerIn.swingArm(handIn);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }else { return  ActionResult.resultFail(playerIn.getHeldItem(handIn)); }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
