package com.example.examplemod.entity.ai.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class ProxyClassGoal extends Goal implements IForgeRegistryEntry<ProxyClassGoal> {

    //Proxy Class for Registry
    @Override
    public boolean shouldExecute() {
        return false;
    }

    @Override
    public ProxyClassGoal setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    @Override
    public Class<ProxyClassGoal> getRegistryType() {
        return null;
    }
}
