package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.item.tools.lootmodifiers.BlazingToolCondition;
import com.example.ttmlm.item.tools.lootmodifiers.EnderToolCondition;
import com.example.ttmlm.item.tools.lootmodifiers.FreezingToolCondition;
import com.example.ttmlm.item.tools.lootmodifiers.FrozenGeodeCondition;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.registry.Registry;

public class ModConditions {
    public static final LootConditionType BLAZING_TOOL = new LootConditionType(new BlazingToolCondition.Serializer());
    public static final LootConditionType ENDER_TOOL = new LootConditionType(new EnderToolCondition.Serializer());
    public static final LootConditionType FREEZING_TOOL = new LootConditionType(new FreezingToolCondition.Serializer());
    public static final LootConditionType FROZEN_GEODE = new LootConditionType(new FrozenGeodeCondition.Serializer());



    private static LootConditionType register(String location, LootConditionType conditionType) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, TTMLM.getID(location), conditionType);
    }

    public static void  registerAll(){
        register("is_blazing_tool", BLAZING_TOOL);
        register("is_ender_tool", ENDER_TOOL);
        register("is_freezing_tool", FREEZING_TOOL);
        register("is_frozen_geode", FROZEN_GEODE);
    }
}
