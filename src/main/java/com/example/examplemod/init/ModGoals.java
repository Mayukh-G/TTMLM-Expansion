package com.example.examplemod.init;
import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.ai.goal.HardCreeperSwellGoal;
import com.example.examplemod.entity.ai.goal.ProxyClassGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;


public class ModGoals {
    //private static ProxyClassGoal hardcreepergoal = new ProxyClassGoal();

    public static void registerALL(RegistryEvent.Register<ProxyClassGoal> event){
        //register("hard_creeper_swell_goal", hardcreepergoal);
    }

    public static <T extends ProxyClassGoal> T register(String name, T goal){
        ResourceLocation id = ExampleMod.getID(name);
        goal.setRegistryName(id);
        //ModRegistries.GOALS_LAZY.getValue().register(goal);
        return goal;
    }
}
