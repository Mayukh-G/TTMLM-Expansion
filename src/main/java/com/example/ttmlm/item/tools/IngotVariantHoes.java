package com.example.ttmlm.item.tools;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class IngotVariantHoes extends HoeItem {
    private IngotVariants variant;

    public IngotVariantHoes(IItemTier tier, float attackSpeedIn, IngotVariants variant, Properties builder) {
        super(tier, attackSpeedIn, builder.group(TTMLM.ITEM_GROUP_TOOL));
        this.variant = variant;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        switch (variant){
            case FREEZING_ALLOY:
                ITextComponent freezInfo = new StringTextComponent("Now that's cold! : ")
                        .applyTextStyles(TextFormatting.BLUE, TextFormatting.ITALIC);
                freezInfo.appendSibling(new StringTextComponent("Right click to freeze water or lava").applyTextStyle(TextFormatting.LIGHT_PURPLE));
                tooltip.add(freezInfo);
                break;
            case BLAZING_ALLOY:
            case WEAK_BLAZING_AllOY:
                tooltip.add(new StringTextComponent("\u00A76\u00A7oToasty! :\u00A7d Right click to shoot a fireball"));
                break;
            case ENDER_ALLOY:
            case WEAK_ENDER_ALLOY:
                ITextComponent enderBaseInfo = new StringTextComponent("But How!? : Right click to toss an ender pearl")
                        .applyTextStyles(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                tooltip.add(enderBaseInfo);

        }
    }

    // Since onItemUse does not work on fluids, this had to be done separately
    // Finds Water and replaces it and other water in an area around it with ice
    @NotNull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        if(!worldIn.isRemote ) {
            if (this.variant == IngotVariants.FREEZING_ALLOY) {
                double x = playerIn.getLookVec().x;
                double y = playerIn.getLookVec().y;
                double z = playerIn.getLookVec().z;
                int checked = 0;
                Vec3d start = playerIn.getPositionVec().add(0, 1.5, 0);
//            TTMLM.LOGGER.debug(start);
//            TTMLM.LOGGER.debug(playerIn.getLookVec());
                BlockPos checkingPos = new BlockPos(start);
                boolean found = false;
                for (int i = 1; checked < 4 && !found; i++) {
                    double tempX = start.x + x * i;
                    double tempY = start.y + y * i;
                    double tempZ = start.z + z * i;
                    BlockPos temp = new BlockPos(tempX, tempY, tempZ);
                    if (temp.getX() != checkingPos.getX() || temp.getY() != checkingPos.getY() || temp.getZ() != checkingPos.getZ()) {
                        checkingPos = temp;
                        IFluidState state = worldIn.getFluidState(checkingPos);
                        if (state == Fluids.WATER.getStillFluidState(false) || state == Fluids.WATER.getStillFluidState(true)
                                || state == Fluids.LAVA.getStillFluidState(false) || state == Fluids.LAVA.getStillFluidState(true)) {
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
                if (found) {
                    playerIn.swing(handIn, true);
                    playerIn.getCooldownTracker().setCooldown(this, 5);
                    playerIn.getHeldItemMainhand().damageItem(10, playerIn, (animation) -> { animation.sendBreakAnimation(handIn);});
                    return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
                } else {
                    return ActionResult.resultFail(playerIn.getHeldItem(handIn));
                }
            }
            else if ((this.variant == IngotVariants.ENDER_ALLOY || this.variant == IngotVariants.WEAK_ENDER_ALLOY) && !playerIn.isCrouching()){ // Throw an Ender Pearl
                EnderPearlEntity pearlEntity = new EnderPearlEntity(worldIn, playerIn);
                pearlEntity.setMotion(playerIn.getLookVec().mul(1.5D, 1.5D, 1.5D));
                worldIn.addEntity(pearlEntity);
                worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 1.0F,1.0F);
                playerIn.swing(handIn, true);
                playerIn.getCooldownTracker().setCooldown(this, 50);
                playerIn.getHeldItemMainhand().damageItem(15, playerIn, (animation) -> { animation.sendBreakAnimation(handIn);});
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }
            else if ((this.variant == IngotVariants.BLAZING_ALLOY|| this.variant == IngotVariants.WEAK_BLAZING_AllOY) && !playerIn.isCrouching()){ // Shoot Fireball
                Random rand = new Random();
                double aX = playerIn.getLookVec().x + rand.nextGaussian() * 0.05;
                double aY = playerIn.getLookVec().y + rand.nextGaussian() * 0.05;
                double aZ = playerIn.getLookVec().z + rand.nextGaussian() * 0.05;
                double fx = playerIn.getPosX() + playerIn.getLookVec().x * 1.5D;
                double fy = playerIn.getPosY() + 1.5;
                double fz = playerIn.getPosZ() + playerIn.getLookVec().z * 1.5D;
                SmallFireballEntity fireballEntity = new SmallFireballEntity(worldIn, fx, fy, fz , aX, aY, aZ);
                worldIn.addEntity(fireballEntity);
                worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                playerIn.swing(handIn, true);
                playerIn.getCooldownTracker().setCooldown(this, 10);
                playerIn.getHeldItemMainhand().damageItem(5, playerIn, (animation) -> { animation.sendBreakAnimation(handIn);});
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
