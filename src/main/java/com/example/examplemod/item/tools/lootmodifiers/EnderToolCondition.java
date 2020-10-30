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

public class EnderToolCondition implements ILootCondition {
    private static EnderToolCondition INSTANCE = new EnderToolCondition();

    private EnderToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        Item held = Objects.requireNonNull(lootContext.get(LootParameters.TOOL)).getItem();
        if(held instanceof ToolItem){
            ToolItem tool = (ToolItem) held;
            return tool.getTier() == IngotVariantTiers.ENDER || tool.getTier() == IngotVariantTiers.WEAK_ENDER;
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    public static class Serializer extends ILootCondition.AbstractSerializer<EnderToolCondition> {
        public Serializer() {
            super(ExampleMod.getID("is_ender_tool"), EnderToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull EnderToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public EnderToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return EnderToolCondition.INSTANCE;
        }
    }

}
