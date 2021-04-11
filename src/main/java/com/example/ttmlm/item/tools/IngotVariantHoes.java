package com.example.ttmlm.item.tools;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.common.extensions.IForgeFluidState;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
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
        super(tier,1 , attackSpeedIn, builder.tab(TTMLM.ITEM_GROUP_TOOL));
        this.variant = variant;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        switch (variant){
            case FREEZING_ALLOY:
                IFormattableTextComponent freezInfo = new StringTextComponent("Now that's cold! : ")
                        .withStyle(TextFormatting.BLUE, TextFormatting.ITALIC);
                freezInfo.append(new StringTextComponent("Right click to freeze water or lava").withStyle(TextFormatting.LIGHT_PURPLE));
                tooltip.add(freezInfo);
                break;
            case BLAZING_ALLOY:
                tooltip.add(new StringTextComponent("\u00A76\u00A7oToasty! :\u00A7d Right click to shoot a fireball"));
                break;
            case ENDER_ALLOY:
                ITextComponent enderBaseInfo = new StringTextComponent("But How!? : Right click to toss an ender pearl")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                tooltip.add(enderBaseInfo);

        }
    }

    @Override
    public float getAttackDamage() {
        return 1;
    }

    // Since onItemUse does not work on fluids, this had to be done separately
    // Finds Water and replaces it and other water in an area around it with ice

    @NotNull
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        if(!worldIn.isClientSide) {
            if (this.variant == IngotVariants.FREEZING_ALLOY) {
                double x = playerIn.getLookAngle().x;
                double y = playerIn.getLookAngle().y;
                double z = playerIn.getLookAngle().z;
                int checked = 0;
                Vector3d start = playerIn.position().add(0, 1.5, 0);
//            TTMLM.LOGGER.debug(start);
//            TTMLM.LOGGER.debug(playerIn.getLookAngle()());
                BlockPos checkingPos = new BlockPos(start);
                boolean found = false;
                for (int i = 1; checked < 4 && !found; i++) {
                    double tempX = start.x + x * i;
                    double tempY = start.y + y * i;
                    double tempZ = start.z + z * i;
                    BlockPos temp = new BlockPos(tempX, tempY, tempZ);
                    if (temp.getX() != checkingPos.getX() || temp.getY() != checkingPos.getY() || temp.getZ() != checkingPos.getZ()) {
                        checkingPos = temp;
                        IForgeFluidState state = worldIn.getFluidState(checkingPos);
                        if (state == Fluids.WATER.getSource(false) || state == Fluids.WATER.getSource(true)
                                || state == Fluids.LAVA.getSource(false) || state == Fluids.LAVA.getSource(true)) {
                            found = true;
                            IForgeFluidState checkStateNoFall = (state == Fluids.LAVA.defaultFluidState()) ? Fluids.LAVA.getSource(false) : Fluids.WATER.getSource(false);
                            IForgeFluidState checkStateFall = (state == Fluids.LAVA.defaultFluidState()) ? Fluids.LAVA.getSource(true) : Fluids.WATER.getSource(true);
                            BlockState replaceState = (state == Fluids.LAVA.defaultFluidState()) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.ICE.defaultBlockState();
                            SoundEvent sound = (state == Fluids.LAVA.defaultFluidState()) ? SoundEvents.LAVA_EXTINGUISH : SoundEvents.GLASS_BREAK;
                            worldIn.setBlockAndUpdate(checkingPos, replaceState);
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
                                    worldIn.setBlockAndUpdate(position, replaceState);
                                }
                            }
                        }
                        checked++;
                    }
                }// for end
                if (found) {
                    playerIn.swing(handIn, true);
                    playerIn.getCooldowns().addCooldown(this, 5);
                    playerIn.getMainHandItem().setDamageValue(10);
                    return ActionResult.success(playerIn.getItemInHand(handIn));
                } else {
                    return ActionResult.fail(playerIn.getItemInHand(handIn));
                }
            }
            else if ((this.variant == IngotVariants.ENDER_ALLOY) && !playerIn.isCrouching()){ // Throw an Ender Pearl
                EnderPearlEntity pearlEntity = new EnderPearlEntity(worldIn, playerIn);
                pearlEntity.setDeltaMovement(playerIn.getLookAngle().multiply(1.5D, 1.5D, 1.5D));
                worldIn.addFreshEntity(pearlEntity);
                worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 1.0F,1.0F);
                playerIn.swing(handIn, true);
                playerIn.getCooldowns().addCooldown(this, 50);
                playerIn.getMainHandItem().setDamageValue(15);
                return ActionResult.success(playerIn.getItemInHand(handIn));
            }
            else if ((this.variant == IngotVariants.BLAZING_ALLOY) && !playerIn.isCrouching()){ // Shoot Fireball
                Random rand = new Random();
                double aX = playerIn.getLookAngle().x + rand.nextGaussian() * 0.05;
                double aY = playerIn.getLookAngle().y + rand.nextGaussian() * 0.05;
                double aZ = playerIn.getLookAngle().z + rand.nextGaussian() * 0.05;
                double fx = playerIn.getX() + playerIn.getLookAngle().x * 1.5D;
                double fy = playerIn.getY() + 1.5;
                double fz = playerIn.getZ() + playerIn.getLookAngle().z * 1.5D;
                SmallFireballEntity fireballEntity = new SmallFireballEntity(worldIn, fx, fy, fz , aX, aY, aZ);
                worldIn.addFreshEntity(fireballEntity);
                worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                playerIn.swing(handIn, true);
                playerIn.getCooldowns().addCooldown(this, 10);
                playerIn.getMainHandItem().setDamageValue(5);
                return ActionResult.success(playerIn.getItemInHand(handIn));
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
