package com.example.examplemod.item.tools.lootmodifiers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariantTiers;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;


public class FreezingToolCondition implements ILootCondition {
    private static FreezingToolCondition INSTANCE = new FreezingToolCondition();
    private final ImmutableList<BlockState> ALLOWED_BLOCKS = ImmutableList.of(
            Blocks.ANDESITE.getDefaultState(),
            Blocks.DIORITE.getDefaultState(),
            Blocks.GRANITE.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
            Blocks.SAND.getDefaultState(),
            Blocks.RED_SAND.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.RED_SANDSTONE.getDefaultState()
    );


    private FreezingToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack heldStack = (lootContext.get(LootParameters.TOOL));
        BlockState state = lootContext.get(LootParameters.BLOCK_STATE);
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

    public static class Serializer extends AbstractSerializer<FreezingToolCondition> {
        public Serializer() {
            super(ExampleMod.getID("is_freezing_tool"), FreezingToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull FreezingToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public FreezingToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return FreezingToolCondition.INSTANCE;
        }
    }

}
