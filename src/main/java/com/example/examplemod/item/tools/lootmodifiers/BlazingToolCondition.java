package com.example.examplemod.item.tools.lootmodifiers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariantTiers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlazingToolCondition implements ILootCondition {
    private static BlazingToolCondition INSTANCE = new BlazingToolCondition();

    private BlazingToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        Item held = Objects.requireNonNull(lootContext.get(LootParameters.TOOL)).getItem();
        if(held instanceof ToolItem){
            ToolItem tool = (ToolItem) held;
            return tool.getTier() == IngotVariantTiers.VARIANT_BLAZING;
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    public static class Serializer extends ILootCondition.AbstractSerializer<BlazingToolCondition> {
        public Serializer() {
            super(ExampleMod.getID("is_blazing_tool"), BlazingToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull BlazingToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public BlazingToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return BlazingToolCondition.INSTANCE;
        }
    }

}
