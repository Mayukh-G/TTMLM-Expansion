package com.example.ttmlm.item.tools;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariantTiers;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.item.tools.capabilities.ESLCapability;
import com.example.ttmlm.item.tools.capabilities.EnderStorageLinker;
import com.example.ttmlm.item.tools.capabilities.IEnderStorageLink;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;


public class IngotVariantPickaxes extends PickaxeItem {

    private final IngotVariants variant;

    public IngotVariantPickaxes(IItemTier tier, int attackDamageIn, float attackSpeedIn, IngotVariants variant, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder.tab(TTMLM.ITEM_GROUP_TOOL));
        this.variant = variant;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (stack.getItem() instanceof ToolItem){
            ToolItem tool = (ToolItem) stack.getItem();
            return (tool.getTier() == IngotVariantTiers.ENDER) ? new EnderStorageLinker(stack) : null;
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        // This is called during building and when player hovers over item in inventory
        switch (this.variant){
            case ENDER_ALLOY:
                IFormattableTextComponent linkedStatus;
                IFormattableTextComponent containerInfo;
                IFormattableTextComponent preLinkedStatus = new StringTextComponent("Linked Status : ")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                IFormattableTextComponent preContainerInfo = new StringTextComponent("Linked Container : ")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                IFormattableTextComponent BaseInfo = new StringTextComponent("Ender Touch : Teleports what you mine!")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                IFormattableTextComponent BaseInfo2 = new StringTextComponent("Ender Link : Link/UnLink to a Chest! (Crouch + RMB)")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);

                if(p_77624_1_.getTag() != null && p_77624_2_ !=null){
                    LazyOptional<IEnderStorageLink> optional = p_77624_1_.getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
                    IEnderStorageLink linker = optional.orElseThrow(Error::new);
                    ChestTileEntity container = linker.getContainer(p_77624_2_);
                    if(container != null){
                        linkedStatus = new StringTextComponent("[LINKED]").withStyle(TextFormatting.GREEN, TextFormatting.BOLD);
                        BlockPos pos = container.getBlockPos();
                        String temp = String.format("Chest at [x:%d, y:%d, z:%d]", pos.getX(), pos.getY(), pos.getZ());
                        containerInfo = new StringTextComponent(temp).withStyle(TextFormatting.GREEN, TextFormatting.BOLD);
                    }else {
                        linkedStatus = new StringTextComponent("[UNLINKED]").withStyle(TextFormatting.RED, TextFormatting.BOLD);
                        containerInfo = new StringTextComponent("[NONE]").withStyle(TextFormatting.RED, TextFormatting.BOLD);
                    }
                }else {
                    linkedStatus = new StringTextComponent("[UNLINKED]").withStyle(TextFormatting.RED, TextFormatting.BOLD);
                    containerInfo = new StringTextComponent("[NONE]").withStyle(TextFormatting.RED, TextFormatting.BOLD);
                }
                p_77624_3_.add(BaseInfo);
                p_77624_3_.add(BaseInfo2);
                p_77624_3_.add(preLinkedStatus.append(linkedStatus));
                p_77624_3_.add(preContainerInfo.append(containerInfo));

                break;
            case BLAZING_ALLOY:
                p_77624_3_.add(new StringTextComponent("\u00A76\u00A7oBlazing Touch :\u00A7d Cook what you mine!"));
                break;
            case FREEZING_ALLOY:
                IFormattableTextComponent freezInfo = new StringTextComponent("Freezing Touch : ")
                        .withStyle(TextFormatting.BLUE, TextFormatting.ITALIC);
                freezInfo.append(new StringTextComponent("Geode drops! [13%] per Block").withStyle(TextFormatting.LIGHT_PURPLE));
                p_77624_3_.add(freezInfo);
                break;
            case WEAK_FREEZING_ALLOY:
                IFormattableTextComponent wfreezInfo = new StringTextComponent("Freezing Touch : ")
                        .withStyle(TextFormatting.BLUE, TextFormatting.ITALIC);
                wfreezInfo.append(new StringTextComponent("Geode drops! [4%] per Block").withStyle(TextFormatting.LIGHT_PURPLE));
                p_77624_3_.add(wfreezInfo);
                break;
            case WEAK_ENDER_ALLOY:
                p_77624_3_.add(new StringTextComponent("Ender Touch : Teleports what you mine!")
                        .withStyle(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC));
        }
    }

    @NotNull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        // Check if Correct Action is being performed
        if(!context.getLevel().isClientSide && context.getItemInHand().getItem() instanceof ToolItem){
            if(((ToolItem) context.getItemInHand().getItem()).getTier() == IngotVariantTiers.ENDER){
                if(context.getPlayer().isCrouching()) {
                    // Setting local variables
                    World world = context.getLevel();
                    PlayerEntity player = context.getPlayer();
                    BlockPos pos = context.getClickedPos();
                    ItemStack tool = context.getItemInHand();
                    LazyOptional<IEnderStorageLink> optional = tool
                            .getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
                    // Catch just in case Optional is not initialized
                    try {
                        // Storing linker and Chest entity and Status msgs
                        IEnderStorageLink linker = optional.orElseThrow(Exception::new);
                        TileEntity te = world.getBlockEntity(pos);
                        //Text defined here for readability
                        StringTextComponent warn =
                                new StringTextComponent("\u00A7e[Warn] Not A Valid Block to Link to");
                        StringTextComponent successLink = new StringTextComponent(
                                String.format("\u00A7aLinked to Chest at [x:%d, y:%d, z:%d]",
                                        pos.getX(), pos.getY(), pos.getZ())
                        );
                        StringTextComponent successUnLink = new StringTextComponent(
                                String.format("\u00A7cUnlinked from Chest at [x:%d, y:%d, z:%d]",
                                        pos.getX(), pos.getY(), pos.getZ())
                        );
                        //Checking if Chest entity is actually a chest
                        if (te instanceof ChestTileEntity) {
                            if(linker.link((ChestTileEntity) te)){
                                player.displayClientMessage(successLink, true);
                            }else {
                                player.displayClientMessage(successUnLink, true);
                            }
                            world.playSound(player, pos, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                            return ActionResultType.SUCCESS;
                        } else {
                            player.displayClientMessage(warn, true);
                            return ActionResultType.FAIL;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return super.useOn(context);
    }
}
