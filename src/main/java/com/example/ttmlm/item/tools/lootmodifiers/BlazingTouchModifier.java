package com.example.ttmlm.item.tools.lootmodifiers;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class BlazingTouchModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected BlazingTouchModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //Check if Loot dropped
        if (!generatedLoot.isEmpty() && !context.getWorld().isRemote) {
            for (ItemStack stack : generatedLoot) {
                //Getting recipes from world (datapacks)
                Optional<FurnaceRecipe> optional = context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld());
                if (optional.isPresent()) {
                    // Getting result of recipes
                    ItemStack outputStack = optional.get().getRecipeOutput();
                    if (!outputStack.isEmpty()) {
                        int gained = stack.getCount() * outputStack.getCount();
                        // Setting Result to correct amount + replacing original ItemStack in list
                        ItemStack finalStack = outputStack.copy();
                        finalStack.setCount(gained);
                        generatedLoot.remove(stack);
                        generatedLoot.add(finalStack);
                    }
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<BlazingTouchModifier> {
        @Override
        public BlazingTouchModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new BlazingTouchModifier(conditionsIn);
        }
    }
}
