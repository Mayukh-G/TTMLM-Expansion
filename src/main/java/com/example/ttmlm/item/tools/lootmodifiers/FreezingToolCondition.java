package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.IngotVariantTiers;
import com.example.ttmlm.init.ModConditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;


public class FreezingToolCondition implements ILootCondition {
    private static FreezingToolCondition INSTANCE = new FreezingToolCondition();
    private final ImmutableList<BlockState> ALLOWED_BLOCKS = ImmutableList.of(
            Blocks.ANDESITE.defaultBlockState(),
            Blocks.DIORITE.defaultBlockState(),
            Blocks.GRANITE.defaultBlockState(),
            Blocks.STONE.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            Blocks.GRAVEL.defaultBlockState(),
            Blocks.SAND.defaultBlockState(),
            Blocks.RED_SAND.defaultBlockState(),
            Blocks.SANDSTONE.defaultBlockState(),
            Blocks.RED_SANDSTONE.defaultBlockState()
    );


    private FreezingToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack heldStack = (lootContext.getParamOrNull(LootParameters.TOOL));
        BlockState state = lootContext.getParamOrNull(LootParameters.BLOCK_STATE);
        if (heldStack != null && ALLOWED_BLOCKS.contains(state)){
            Item held = heldStack.getItem();
            if(held instanceof ToolItem && !(held instanceof AxeItem)){
                ToolItem tool = (ToolItem) held;
                IItemTier tier = tool.getTier();
                return tier == IngotVariantTiers.VARIANT_FREEZING || tier == IngotVariantTiers.WEAK_VARIANT_FREEZING;
            }
        }
        return false;
    }

    public static IBuilder builder() {
        return () -> INSTANCE;
    }

    @NotNull
    @Override
    public LootConditionType getType() {
        return ModConditions.FREEZING_TOOL;
    }

    public static class Serializer implements ILootSerializer<FreezingToolCondition> {
        public Serializer() {
//            super(TTMLM.getID("is_freezing_tool"), FreezingToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull FreezingToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public FreezingToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return FreezingToolCondition.INSTANCE;
        }
    }

}
