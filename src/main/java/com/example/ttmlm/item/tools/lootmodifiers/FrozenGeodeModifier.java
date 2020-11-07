package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.IngotVariantTiers;
import com.google.gson.JsonObject;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class FrozenGeodeModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected FrozenGeodeModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        IItemTier tier = ((ToolItem) context.get(LootParameters.TOOL).getItem()).getTier();
        if(!(tier == IngotVariantTiers.ENDER || tier == IngotVariantTiers.WEAK_ENDER)) {
            Random random = new Random();
            int choice = random.nextInt(100); //0-99
            int amm = random.nextInt(5);
            ItemStack stack;
            // Basically just sets the loot to be one of the following and randomizes the amm from 1-5
            generatedLoot.remove(0);
            amm++;
            if (choice <= 29) { // 30%
                stack = new ItemStack(Items.CLAY_BALL);
                stack.setCount(amm);
                generatedLoot.add(stack);
            } else if (choice <= 54) { //25%
                stack = new ItemStack(Items.COAL);
                stack.setCount(amm);
                generatedLoot.add(stack);
            } else if (choice <= 74) { //20%
                stack = new ItemStack(Items.IRON_INGOT);
                stack.setCount(amm);
                generatedLoot.add(stack);
            } else if (choice <= 84) { //10%
                stack = new ItemStack(Items.GOLD_INGOT);
                stack.setCount(amm);
                generatedLoot.add(stack);
            } else if (choice <= 94) { //10%
                stack = new ItemStack(Items.LAPIS_LAZULI);
                stack.setCount(amm);
                generatedLoot.add(stack);
            } else if (choice <= 99) { //5%
                stack = new ItemStack(Items.DIAMOND);
                stack.setCount(amm);
                generatedLoot.add(stack);
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FrozenGeodeModifier> {
        @Override
        public FrozenGeodeModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new FrozenGeodeModifier(conditionsIn);
        }
    }
}
