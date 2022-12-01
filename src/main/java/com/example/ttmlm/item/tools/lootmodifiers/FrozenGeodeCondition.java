package com.example.ttmlm.item.tools.lootmodifiers;

import com.example.ttmlm.init.ModBlocks;
import com.example.ttmlm.init.ModConditions;
import com.example.ttmlm.init.ModEntities;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;

public class FrozenGeodeCondition implements ILootCondition {

    private static FrozenGeodeCondition INSTANCE = new FrozenGeodeCondition();

    FrozenGeodeCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        if(lootContext.hasParam(LootParameters.TOOL) && lootContext.hasParam(LootParameters.BLOCK_STATE)){
            return lootContext.getParamOrNull(LootParameters.BLOCK_STATE).getBlock() == ModBlocks.frozenGeode &&
                    lootContext.getParamOrNull(LootParameters.TOOL).getItem() instanceof PickaxeItem;
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    @NotNull
    @Override
    public LootConditionType getType() {
        return ModConditions.FROZEN_GEODE;
    }

    public static class Serializer implements ILootSerializer<FrozenGeodeCondition> {
        public Serializer() {
//            super(TTMLM.getID("is_frozen_geode"), FrozenGeodeCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull FrozenGeodeCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public FrozenGeodeCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return FrozenGeodeCondition.INSTANCE;
        }
    }
}
