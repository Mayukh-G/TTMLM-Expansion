package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.ai.goal.ProxyClassGoal;
import net.minecraft.util.LazyValue;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class ModRegistries {

    public static LazyValue<IForgeRegistry<?>> GOALS_LAZY;

    public static void registerALL(RegistryEvent.NewRegistry event){
        //Goals registry
        RegistryBuilder<ProxyClassGoal> goals = new RegistryBuilder<ProxyClassGoal>();
        goals.setName(ExampleMod.getID("_goals_registry"));
        goals.setType(ProxyClassGoal.class);
        goals.allowModification();

        GOALS_LAZY = new LazyValue<>(goals::create);
    }
}
