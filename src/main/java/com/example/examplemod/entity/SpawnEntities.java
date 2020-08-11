package com.example.examplemod.entity;

import com.example.examplemod.entity.changed.*;
import com.example.examplemod.init.ModEntities;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SpawnEntities {

    public static void trySpawning(EntityJoinWorldEvent event){
        if(!event.getWorld().isRemote() && (event.getEntity() instanceof MonsterEntity || event.getEntity() instanceof FlyingEntity)){
            Entity preEntity = event.getEntity();
            //Check is entity is inside chunk
            if (preEntity.addedToChunk){
                return;
            }
            //.get() functions are slightly slow if used too much, minimising use by storing into variables
            World world = event.getWorld();
            double X = preEntity.getPosX();
            double Y = preEntity.getPosY();
            double Z = preEntity.getPosZ();
            if(event.getEntity() instanceof FlyingEntity){
                FlyingEntity entity = (FlyingEntity) preEntity;
                if(entity instanceof GhastEntity && !(entity instanceof HardGhast)){
                    //Summon Ghast and set pos
                    HardGhast ghast = new HardGhast(ModEntities.HARD_GHAST, world);
                    ghast.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    ghast.onInitialSpawn(world, world.getDifficultyForLocation(ghast.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addEntity(ghast);
                    event.setCanceled(true);
                }

            }else { //WHEN ITS A MONSTER ENTITY
                MonsterEntity entity = (MonsterEntity) preEntity;
                //If zombie
                if(entity instanceof ZombieEntity && !(entity instanceof IAbstractHardZombie)){
                    if(entity.getType() == EntityType.HUSK){
                        //Summon Husk and set pos
                        HardHusk husk = new HardHusk(ModEntities.HARD_HUSK, world);
                        husk.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        husk.onInitialSpawn(world, world.getDifficultyForLocation(husk.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        if(husk.isLEADER){
                            husk.setLeaderAttributes();
                            husk.setCustomName(husk.leaderName);
                            husk.setCustomNameVisible(true);
                        }
                        world.addEntity(husk);
                    }else if(entity.getType() == EntityType.ZOMBIE_PIGMAN){
                        //Summon ZPigman and set pos
                        HardZPigMan pigZombie = new HardZPigMan(ModEntities.HARD_Z_PIGMAN, world);
                        pigZombie.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        pigZombie.onInitialSpawn(world, world.getDifficultyForLocation(pigZombie.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        if(pigZombie.isLEADER){
                            pigZombie.setLeaderAttributes();
                            pigZombie.setCustomName(pigZombie.leaderName);
                            pigZombie.setCustomNameVisible(true);
                        }
                        world.addEntity(pigZombie);

                    }else if(entity.getType() == EntityType.ZOMBIE_VILLAGER){
                        //not going to change them, because they are not true zombies they are weak I guess
                        return;
                    }else if (entity.getType() == EntityType.DROWNED){
                        //Summon Drowned
                        HardDrowned drowned = new HardDrowned(ModEntities.HARD_DROWNED, world);
                        drowned.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        drowned.onInitialSpawn(world, world.getDifficultyForLocation(drowned.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        if(drowned.isLEADER){ //FIX THIS when u leave the world it resets name,
                            drowned.setLeaderAttributes();
                            drowned.setCustomName(drowned.leaderName); // add this to tick or something
                            drowned.setCustomNameVisible(true);
                        }
                        world.addEntity(drowned);
                    }
                    else {
                        //summon Zombie and set pos
                        HardZombie zombie = new HardZombie(ModEntities.HARD_ZOMBIE, world);
                        zombie.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        zombie.onInitialSpawn(world, world.getDifficultyForLocation(zombie.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        if(zombie.isLEADER){
                            zombie.setLeaderAttributes();
                            zombie.setCustomName(zombie.leaderName);
                            zombie.setCustomNameVisible(true);
                        }
                        world.addEntity(zombie);
                    }
                    event.setCanceled(true);
                }
                //If Skeleton
                else if(entity instanceof AbstractSkeletonEntity && !(entity instanceof HardSkeleton) && !(entity instanceof HardStray) && !(entity instanceof HardWitherSkeleton)){
                    if(entity instanceof StrayEntity){
                        //summon Stray and set pos and call on spawn initial func
                        HardStray stray = new HardStray(ModEntities.HARD_STRAY, world);
                        stray.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        stray.onInitialSpawn(world, world.getDifficultyForLocation(stray.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addEntity(stray);
                    }
                    else if(entity instanceof WitherSkeletonEntity){
                        //summon WitherSkeleton and set pos and call on spawn initial func
                        HardWitherSkeleton witherSkeleton = new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, world);
                        witherSkeleton.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        witherSkeleton.onInitialSpawn(world, world.getDifficultyForLocation(witherSkeleton.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addEntity(witherSkeleton);
                    }
                    else {
                        //summon Skeleton and set pos and call on spawn initial func
                        HardSkeleton skeleton = new HardSkeleton(ModEntities.HARD_SKELETON, world);
                        skeleton.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        skeleton.onInitialSpawn(world, world.getDifficultyForLocation(skeleton.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addEntity(skeleton);
                    }
                    event.setCanceled(true);
                }
                //If Creeper
                else if (entity instanceof CreeperEntity && !(entity instanceof HardCreeper)){
                    //summon creeper and set pos
                    HardCreeper creeper = new HardCreeper(ModEntities.HARD_CREEPER, world);
                    creeper.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    world.addEntity(creeper);
                    event.setCanceled(true);
                }
                else if (entity instanceof SpiderEntity && !(entity instanceof HardSpider) && !(entity instanceof HardCaveSpider)){
                    if(entity instanceof CaveSpiderEntity){
                        HardCaveSpider caveSpider = new HardCaveSpider(ModEntities.HARD_CAVE_SPIDER, world);
                        caveSpider.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        caveSpider.onInitialSpawn(world, world.getDifficultyForLocation(caveSpider.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addEntity(caveSpider);
                    }else {
                        HardSpider spider = new HardSpider(ModEntities.HARD_SPIDER, world);
                        spider.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                        spider.onInitialSpawn(world, world.getDifficultyForLocation(spider.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addEntity(spider);
                    }
                    event.setCanceled(true);
                }
                else if(entity instanceof EndermanEntity && !(entity instanceof HardEnderman)){
                    HardEnderman enderman = new HardEnderman(ModEntities.HARD_ENDERMAN, world);
                    enderman.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    enderman.onInitialSpawn(world, world.getDifficultyForLocation(enderman.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addEntity(enderman);
                    event.setCanceled(true);
                }
                else if(entity instanceof BlazeEntity && !(entity instanceof HardBlaze)){
                    HardBlaze blaze = new HardBlaze(ModEntities.HARD_BLAZE, world);
                    blaze.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    blaze.onInitialSpawn(world, world.getDifficultyForLocation(blaze.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addEntity(blaze);
                    event.setCanceled(true);
                }
            }
        }
    }
}
