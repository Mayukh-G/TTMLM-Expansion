package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.IngotVariantTiers;
import com.example.ttmlm.init.ModBlocks;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class FreeingTouchModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected FreeingTouchModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        //Check if Loot dropped and check for nulls
        if (!generatedLoot.isEmpty() && !context.getLevel().isClientSide) {
            ItemStack stack = context.getParamOrNull(LootParameters.TOOL);
            if(stack != null){
                if(stack.getItem() instanceof ToolItem){
                    // Determining which Tier
                    boolean is_weak_freeze = ((ToolItem) stack.getItem()).getTier() == IngotVariantTiers.WEAK_VARIANT_FREEZING;
                    Random random = new Random();
                    int randInt = random.nextInt(100);
                    // Sets odds depending on Tool Tier
                    int pass_check = (is_weak_freeze) ? 3 : 12; // 4% , 13%
                    // Remove whatever you were going to get
                    if(randInt <= pass_check){
                        for(int i = 0; generatedLoot.size() > 0; i++){ generatedLoot.remove(i); }
                        generatedLoot.add(new ItemStack(ModBlocks.frozenGeode));
                    }
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<FreeingTouchModifier> {
        @Override
        public FreeingTouchModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new FreeingTouchModifier(conditionsIn);
        }

        @Override
        public JsonObject write(FreeingTouchModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
