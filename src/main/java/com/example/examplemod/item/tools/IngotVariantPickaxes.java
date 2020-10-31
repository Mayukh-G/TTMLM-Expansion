package com.example.examplemod.item.tools;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariantTiers;
import com.example.examplemod.init.IngotVariants;
import com.example.examplemod.item.tools.capabilities.ESLCapability;
import com.example.examplemod.item.tools.capabilities.EnderStorageLinker;
import com.example.examplemod.item.tools.capabilities.IEnderStorageLink;
import javafx.concurrent.Worker;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
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
import java.util.Optional;


public class IngotVariantPickaxes extends PickaxeItem {

    private final IngotVariants variant;

    public IngotVariantPickaxes(IItemTier tier, int attackDamageIn, float attackSpeedIn, IngotVariants variant, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder.group(ExampleMod.ITEM_GROUP));
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        // This is called during building and when player hovers over item in inventory
        switch (this.variant){
            case ENDER_ALLOY:
                ITextComponent linkedStatus;
                ITextComponent containerInfo;
                ITextComponent preLinkedStatus = new StringTextComponent("Linked Status : ")
                        .applyTextStyles(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                ITextComponent preContainerInfo = new StringTextComponent("Linked Container : ")
                        .applyTextStyles(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                ITextComponent BaseInfo = new StringTextComponent("Ender Touch : Teleports what you mine!")
                        .applyTextStyles(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);
                ITextComponent BaseInfo2 = new StringTextComponent("Ender Link : Link/UnLink to a Chest! (Crouch + RMB)")
                        .applyTextStyles(TextFormatting.LIGHT_PURPLE, TextFormatting.ITALIC);

                if(stack.getTag() != null && worldIn !=null){
                    LazyOptional<IEnderStorageLink> optional = stack.getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
                    IEnderStorageLink linker = optional.orElseThrow(Error::new);
                    ChestTileEntity container = linker.getContainer(worldIn);
                    if(container != null){
                        linkedStatus = new StringTextComponent("[LINKED]").applyTextStyles(TextFormatting.GREEN, TextFormatting.BOLD);
                        BlockPos pos = container.getPos();
                        String temp = String.format("Chest at [x:%d, y:%d, z:%d]", pos.getX(), pos.getY(), pos.getZ());
                        containerInfo = new StringTextComponent(temp).applyTextStyles(TextFormatting.GREEN, TextFormatting.BOLD);
                    }else {
                        linkedStatus = new StringTextComponent("[UNLINKED]").applyTextStyles(TextFormatting.RED, TextFormatting.BOLD);
                        containerInfo = new StringTextComponent("[NONE]").applyTextStyles(TextFormatting.RED, TextFormatting.BOLD);
                    }
                }else {
                    linkedStatus = new StringTextComponent("[UNLINKED]").applyTextStyles(TextFormatting.RED, TextFormatting.BOLD);
                    containerInfo = new StringTextComponent("[NONE]").applyTextStyles(TextFormatting.RED, TextFormatting.BOLD);
                }
                tooltip.add(BaseInfo);
                tooltip.add(BaseInfo2);
                tooltip.add(preLinkedStatus.appendSibling(linkedStatus));
                tooltip.add(preContainerInfo.appendSibling(containerInfo));

                break;
            case BLAZING_ALLOY:
                tooltip.add(new StringTextComponent("\u00A76\u00A7oBlazing Touch :\u00A7d Cook what you mine!"));
                break;
        }
    }

    @NotNull
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Check if Correct Action is being performed
        if(!context.getWorld().isRemote && context.getItem().getItem() instanceof ToolItem){
            if(((ToolItem) context.getItem().getItem()).getTier() == IngotVariantTiers.ENDER){
                if(context.getPlayer().isCrouching()) {
                    // Setting local variables
                    World world = context.getWorld();
                    PlayerEntity player = context.getPlayer();
                    BlockPos pos = context.getPos();
                    ItemStack tool = context.getItem();
                    LazyOptional<IEnderStorageLink> optional = tool
                            .getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
                    // Catch just in case Optional is not initialized
                    try {
                        // Storing linker and Chest entity and Status msgs
                        IEnderStorageLink linker = optional.orElseThrow(Exception::new);
                        TileEntity te = world.getTileEntity(pos);
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
                                player.sendStatusMessage(successLink, true);
                            }else {
                                player.sendStatusMessage(successUnLink, true);
                            }
                            return ActionResultType.SUCCESS;
                        } else {
                            player.sendStatusMessage(warn, true);
                            return ActionResultType.FAIL;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }
}
