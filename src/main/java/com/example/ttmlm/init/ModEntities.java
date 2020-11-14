package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.*;

import com.example.ttmlm.entity.original.NetherBoss;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unchecked")
public class ModEntities {
    public static EntityType<HardZombie> HARD_ZOMBIE = (EntityType<HardZombie>) EntityType.Builder.create(HardZombie::new, EntityClassification.MONSTER).build(HardZombie.name).setRegistryName(TTMLM.getID(HardZombie.name));
    public static EntityType<HardHusk> HARD_HUSK = (EntityType<HardHusk>) EntityType.Builder.create(HardHusk::new, EntityClassification.MONSTER).build(HardHusk.name).setRegistryName(TTMLM.getID(HardHusk.name));
    public static EntityType<HardDrowned> HARD_DROWNED = (EntityType<HardDrowned>) EntityType.Builder.create(HardDrowned::new, EntityClassification.MONSTER).build(HardDrowned.name).setRegistryName(TTMLM.getID(HardDrowned.name));
    public static EntityType<HardSkeleton> HARD_SKELETON = (EntityType<HardSkeleton>) EntityType.Builder.create(HardSkeleton::new, EntityClassification.MONSTER).build(HardSkeleton.name).setRegistryName(TTMLM.getID(HardSkeleton.name));
    public static EntityType<HardStray> HARD_STRAY = (EntityType<HardStray>) EntityType.Builder.create(HardStray::new, EntityClassification.MONSTER).build(HardStray.name).setRegistryName(TTMLM.getID(HardStray.name));
    public static EntityType<HardCreeper> HARD_CREEPER = (EntityType<HardCreeper>) EntityType.Builder.create(HardCreeper::new, EntityClassification.MONSTER).build(HardCreeper.name).setRegistryName(TTMLM.getID(HardCreeper.name));
    public static EntityType<HardSpider> HARD_SPIDER = (EntityType<HardSpider>) EntityType.Builder.create(HardSpider::new, EntityClassification.MONSTER).size(1.4F, 0.9F).build(HardSpider.name).setRegistryName(TTMLM.getID(HardSpider.name));
    public static EntityType<HardCaveSpider> HARD_CAVE_SPIDER = (EntityType<HardCaveSpider>) EntityType.Builder.create(HardCaveSpider::new, EntityClassification.MONSTER).size(0.7F, 0.5F).build(HardCaveSpider.name).setRegistryName(TTMLM.getID(HardCaveSpider.name));
    public static EntityType<HardEnderman> HARD_ENDERMAN = (EntityType<HardEnderman>) EntityType.Builder.create(HardEnderman::new, EntityClassification.MONSTER).size(0.6F, 2.9F).build(HardEnderman.name).setRegistryName(TTMLM.getID(HardEnderman.name));
    //Nether
    public static EntityType<HardZPigMan> HARD_Z_PIGMAN = (EntityType<HardZPigMan>) EntityType.Builder.create(HardZPigMan::new, EntityClassification.MONSTER).immuneToFire().build(HardZPigMan.name).setRegistryName(TTMLM.getID(HardZPigMan.name));
    public static EntityType<HardGhast> HARD_GHAST = (EntityType<HardGhast>) EntityType.Builder.create(HardGhast::new, EntityClassification.MONSTER).immuneToFire().size(4.0F, 4.0F).build(HardGhast.name).setRegistryName(TTMLM.getID(HardGhast.name));
    public static EntityType<HardWitherSkeleton> HARD_WITHER_SKELETON = (EntityType<HardWitherSkeleton>) EntityType.Builder.create(HardWitherSkeleton::new, EntityClassification.MONSTER).immuneToFire().size(0.7F, 2.4F).build(HardWitherSkeleton.name).setRegistryName(TTMLM.getID(HardWitherSkeleton.name));
    public static EntityType<HardBlaze> HARD_BLAZE = (EntityType<HardBlaze>) EntityType.Builder.create(HardBlaze::new, EntityClassification.MONSTER).immuneToFire().size(0.6F, 1.8F).build(HardBlaze.name).setRegistryName(TTMLM.getID(HardBlaze.name));
    //Boss
    public static EntityType<NetherBoss> NETHER_BOSS = (EntityType<NetherBoss>) EntityType.Builder.create(NetherBoss::new, EntityClassification.MONSTER).immuneToFire().size(1.4F, 4.0F).build(NetherBoss.name).setRegistryName(TTMLM.getID(NetherBoss.name));

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
                HARD_Z_PIGMAN,
                HARD_WITHER_SKELETON,
                HARD_BLAZE,
                NETHER_BOSS
        );
    }
}