package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.entity.changed.*;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings("unchecked")
public class ModEntities {
    public static EntityType<HardZombie> HARD_ZOMBIE = (EntityType<HardZombie>) EntityType.Builder.create(HardZombie::new, EntityClassification.MONSTER).build(HardZombie.name).setRegistryName(ExampleMod.getID(HardZombie.name));
    public static EntityType<HardHusk> HARD_HUSK = (EntityType<HardHusk>) EntityType.Builder.create(HardHusk::new, EntityClassification.MONSTER).build(HardHusk.name).setRegistryName(ExampleMod.getID(HardHusk.name));
    public static EntityType<HardDrowned> HARD_DROWNED = (EntityType<HardDrowned>) EntityType.Builder.create(HardDrowned::new, EntityClassification.MONSTER).build(HardDrowned.name).setRegistryName(ExampleMod.getID(HardDrowned.name));
    public static EntityType<HardSkeleton> HARD_SKELETON = (EntityType<HardSkeleton>) EntityType.Builder.create(HardSkeleton::new, EntityClassification.MONSTER).build(HardSkeleton.name).setRegistryName(ExampleMod.getID(HardSkeleton.name));
    public static EntityType<HardCreeper> HARD_CREEPER = (EntityType<HardCreeper>) EntityType.Builder.create(HardCreeper::new, EntityClassification.MONSTER).build(HardCreeper.name).setRegistryName(ExampleMod.getID(HardCreeper.name));
    //Nether
    public static EntityType<HardZPigMan> HARD_Z_PIGMAN = (EntityType<HardZPigMan>) EntityType.Builder.create(HardZPigMan::new, EntityClassification.MONSTER).immuneToFire().build(HardZPigMan.name).setRegistryName(ExampleMod.getID(HardZPigMan.name));

    public static void registerALL(RegistryEvent.Register<EntityType<?>> event){
        if(!event.getName().equals(ForgeRegistries.ENTITIES.getRegistryName())) return;
        ForgeRegistries.ENTITIES.registerAll(
                HARD_ZOMBIE,
                HARD_Z_PIGMAN,
                HARD_HUSK,
                HARD_DROWNED,
                HARD_SKELETON,
                HARD_CREEPER
        );
    }
}
