package com.example.examplemod.item.tools;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariantTiers;
import com.example.examplemod.init.IngotVariants;
import com.example.examplemod.item.tools.capabilities.EnderStorageLinker;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;


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

}
