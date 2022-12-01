package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.*;

import com.example.ttmlm.entity.original.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unchecked")
public class ModEntities {
    public static EntityType<HardZombie> HARD_ZOMBIE = (EntityType<HardZombie>) EntityType.Builder.of(HardZombie::new, EntityClassification.MONSTER).build(HardZombie.name).setRegistryName(TTMLM.getID(HardZombie.name));
    public static EntityType<HardHusk> HARD_HUSK = (EntityType<HardHusk>) EntityType.Builder.of(HardHusk::new, EntityClassification.MONSTER).build(HardHusk.name).setRegistryName(TTMLM.getID(HardHusk.name));
    public static EntityType<HardDrowned> HARD_DROWNED = (EntityType<HardDrowned>) EntityType.Builder.of(HardDrowned::new, EntityClassification.MONSTER).build(HardDrowned.name).setRegistryName(TTMLM.getID(HardDrowned.name));
    public static EntityType<HardSkeleton> HARD_SKELETON = (EntityType<HardSkeleton>) EntityType.Builder.of(HardSkeleton::new, EntityClassification.MONSTER).build(HardSkeleton.name).setRegistryName(TTMLM.getID(HardSkeleton.name));
    public static EntityType<HardStray> HARD_STRAY = (EntityType<HardStray>) EntityType.Builder.of(HardStray::new, EntityClassification.MONSTER).build(HardStray.name).setRegistryName(TTMLM.getID(HardStray.name));
    public static EntityType<HardCreeper> HARD_CREEPER = (EntityType<HardCreeper>) EntityType.Builder.of(HardCreeper::new, EntityClassification.MONSTER).build(HardCreeper.name).setRegistryName(TTMLM.getID(HardCreeper.name));
    public static EntityType<HardSpider> HARD_SPIDER = (EntityType<HardSpider>) EntityType.Builder.of(HardSpider::new, EntityClassification.MONSTER).sized(1.4F, 0.9F).build(HardSpider.name).setRegistryName(TTMLM.getID(HardSpider.name));
    public static EntityType<HardCaveSpider> HARD_CAVE_SPIDER = (EntityType<HardCaveSpider>) EntityType.Builder.of(HardCaveSpider::new, EntityClassification.MONSTER).sized(0.7F, 0.5F).build(HardCaveSpider.name).setRegistryName(TTMLM.getID(HardCaveSpider.name));
    public static EntityType<HardEnderman> HARD_ENDERMAN = (EntityType<HardEnderman>) EntityType.Builder.of(HardEnderman::new, EntityClassification.MONSTER).sized(0.6F, 2.9F).build(HardEnderman.name).setRegistryName(TTMLM.getID(HardEnderman.name));
    //Nether
    public static EntityType<HardZPiglin> HARD_Z_PIGLIN = (EntityType<HardZPiglin>) EntityType.Builder.of(HardZPiglin::new, EntityClassification.MONSTER).fireImmune().build(HardZPiglin.name).setRegistryName(TTMLM.getID(HardZPiglin.name));
    public static EntityType<HardGhast> HARD_GHAST = (EntityType<HardGhast>) EntityType.Builder.of(HardGhast::new, EntityClassification.MONSTER).fireImmune().sized(4.0F, 4.0F).build(HardGhast.name).setRegistryName(TTMLM.getID(HardGhast.name));
    public static EntityType<HardWitherSkeleton> HARD_WITHER_SKELETON = (EntityType<HardWitherSkeleton>) EntityType.Builder.of(HardWitherSkeleton::new, EntityClassification.MONSTER).fireImmune().sized(0.7F, 2.4F).build(HardWitherSkeleton.name).setRegistryName(TTMLM.getID(HardWitherSkeleton.name));
    public static EntityType<HardBlaze> HARD_BLAZE = (EntityType<HardBlaze>) EntityType.Builder.of(HardBlaze::new, EntityClassification.MONSTER).fireImmune().sized(0.6F, 1.8F).build(HardBlaze.name).setRegistryName(TTMLM.getID(HardBlaze.name));
    //Boss
    public static EntityType<NetherBoss> NETHER_BOSS = (EntityType<NetherBoss>) EntityType.Builder.of(NetherBoss::new, EntityClassification.MONSTER).fireImmune().sized(1.4F, 4.0F).build(NetherBoss.name).setRegistryName(TTMLM.getID(NetherBoss.name));
    //SwarmCallers
    public static EntityType<HardZombieSwarmCaller> HARD_ZOMBIE_SC = (EntityType<HardZombieSwarmCaller>) EntityType.Builder.of(HardZombieSwarmCaller::new, EntityClassification.MONSTER).sized(1.0F, 2.2F).build(HardZombieSwarmCaller.name).setRegistryName(TTMLM.getID(HardZombieSwarmCaller.name));
    public static EntityType<HardHuskSwarmCaller> HARD_HUSK_SC = (EntityType<HardHuskSwarmCaller>) EntityType.Builder.of(HardHuskSwarmCaller::new, EntityClassification.MONSTER).sized(1.0F, 2.2F).build(HardHuskSwarmCaller.name).setRegistryName(TTMLM.getID(HardHuskSwarmCaller.name));
    public static EntityType<HardDrownedSwarmCaller> HARD_DROWNED_SC = (EntityType<HardDrownedSwarmCaller>) EntityType.Builder.of(HardDrownedSwarmCaller::new, EntityClassification.MONSTER).sized(1.0F, 2.2F).build(HardDrownedSwarmCaller.name).setRegistryName(TTMLM.getID(HardDrownedSwarmCaller.name));
    public static EntityType<HardZPiglinSwarmCaller> HARD_Z_PIGLIN_SC = (EntityType<HardZPiglinSwarmCaller>) EntityType.Builder.of(HardZPiglinSwarmCaller::new, EntityClassification.MONSTER).sized(1.0F, 2.2F).fireImmune().build(HardZPiglinSwarmCaller.name).setRegistryName(TTMLM.getID(HardZPiglinSwarmCaller.name));
    public static void registerALL(RegistryEvent.Register<EntityType<?>> event){
        if(!event.getName().equals(ForgeRegistries.ENTITIES.getRegistryName())) return;
        ForgeRegistries.ENTITIES.registerAll(
                HARD_ZOMBIE,
                HARD_HUSK,
                HARD_DROWNED,
                HARD_SKELETON,
                HARD_STRAY,
                HARD_CREEPER,
                HARD_SPIDER,
                HARD_CAVE_SPIDER,
                HARD_ENDERMAN,
                HARD_GHAST,
                HARD_Z_PIGLIN,
                HARD_WITHER_SKELETON,
                HARD_BLAZE,
                NETHER_BOSS,
                HARD_ZOMBIE_SC,
                HARD_HUSK_SC,
                HARD_DROWNED_SC,
                HARD_Z_PIGLIN_SC
        );
    }
}
