package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.IngotVariantTiers;
import com.example.ttmlm.init.ModConditions;
import com.example.ttmlm.init.ModEntities;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;

public class EnderToolCondition implements ILootCondition {
    private static EnderToolCondition INSTANCE = new EnderToolCondition();

    private EnderToolCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack heldStack = (lootContext.getParamOrNull(LootParameters.TOOL));
        if (heldStack != null) {
            Item held = heldStack.getItem();
            if (held instanceof ToolItem) {
                ToolItem tool = (ToolItem) held;
                return tool.getTier() == IngotVariantTiers.ENDER || tool.getTier() == IngotVariantTiers.WEAK_ENDER;
            }
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    @NotNull
    @Override
    public LootConditionType getType() {
        return ModConditions.ENDER_TOOL;
    }

    public static class Serializer implements ILootSerializer<EnderToolCondition> {
        public Serializer() {
//            super(TTMLM.getID("is_ender_tool"), EnderToolCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull EnderToolCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public EnderToolCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return EnderToolCondition.INSTANCE;
        }
    }

}
