package com.example.examplemod.item.tools.lootmodifiers;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModBlocks;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import org.jetbrains.annotations.NotNull;

public class FrozenGeodeCondition implements ILootCondition {

    private static FrozenGeodeCondition INSTANCE = new FrozenGeodeCondition();

    FrozenGeodeCondition(){
    }

    @Override
    public boolean test(LootContext lootContext) {
        if(lootContext.has(LootParameters.TOOL) && lootContext.has(LootParameters.BLOCK_STATE)){
            return lootContext.get(LootParameters.BLOCK_STATE).getBlock() == ModBlocks.frozenGeode &&
                    lootContext.get(LootParameters.TOOL).getItem() instanceof PickaxeItem;
        }
        return false;
    }

    public static ILootCondition.IBuilder builder() {
        return () -> INSTANCE;
    }

    public static class Serializer extends ILootCondition.AbstractSerializer<FrozenGeodeCondition> {
        public Serializer() {
            super(ExampleMod.getID("is_frozen_geode"), FrozenGeodeCondition.class);
        }

        public void serialize(@NotNull JsonObject json, @NotNull FrozenGeodeCondition value, @NotNull JsonSerializationContext context) {
        }

        @NotNull
        public FrozenGeodeCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return FrozenGeodeCondition.INSTANCE;
        }
    }
}
