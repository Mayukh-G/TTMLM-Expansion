package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariantTiers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;

public class BlazingToolCondition implements ILootCondition {
    private static BlazingToolCondition INSTANCE = new BlazingToolCondition();

    private BlazingToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack heldStack = (lootContext.get(LootParameters.TOOL));
        if (heldStack != null){
            Item held = heldStack.getItem();
            if(held instanceof ToolItem){
                ToolItem tool = (ToolItem) held;
                return tool.getTier() == IngotVariantTiers.VARIANT_BLAZING;
            }
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    public static class Serializer extends ILootCondition.AbstractSerializer<BlazingToolCondition> {
        public Serializer() {
            super(TTMLM.getID("is_blazing_tool"), BlazingToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull BlazingToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public BlazingToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return BlazingToolCondition.INSTANCE;
        }
    }

}
