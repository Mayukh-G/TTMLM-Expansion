package com.example.ttmlm.entity;

import com.example.ttmlm.entity.changed.*;
import com.example.ttmlm.entity.original.HardDrownedSwarmCaller;
import com.example.ttmlm.entity.original.HardHuskSwarmCaller;
import com.example.ttmlm.entity.original.HardZPiglinSwarmCaller;
import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class SpawnEntities {

    public static void trySpawning(EntityJoinWorldEvent event){
        if(!event.getWorld().isClientSide && (event.getEntity() instanceof MonsterEntity || event.getEntity() instanceof FlyingEntity)){
            Entity preEntity = event.getEntity();
            //Check is entity is inside chunk
            if (preEntity.isAddedToWorld()){
                return;
            }
            //.get() functions are slightly slow if used too much, minimising use by storing into variables
            ServerWorld world = (ServerWorld) event.getWorld();
            double X = preEntity.getX();
            double Y = preEntity.getY();
            double Z = preEntity.getZ();
            if(event.getEntity() instanceof FlyingEntity){
                FlyingEntity entity = (FlyingEntity) preEntity;
                if(entity instanceof GhastEntity && !(entity instanceof HardGhast)){
                    //Summon Ghast and set pos
                    HardGhast ghast = new HardGhast(ModEntities.HARD_GHAST, world);
                    ghast.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                    ghast.finalizeSpawn(world, world.getCurrentDifficultyAt(ghast.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addFreshEntity(ghast);
                    event.setCanceled(true);
                }

            }else { //WHEN ITS A MONSTER ENTITY
                MonsterEntity entity = (MonsterEntity) preEntity;
                //If zombie
                if(entity instanceof ZombieEntity && !(entity instanceof IAbstractHardZombie)){
                    if(entity.getType() == EntityType.HUSK){
                        //Summon Husk and set pos
                        if (world.random.nextInt(99) <= 4){
                            HardHuskSwarmCaller husk = new HardHuskSwarmCaller(ModEntities.HARD_HUSK_SC, world);
                            husk.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            husk.finalizeSpawn(world, world.getCurrentDifficultyAt(husk.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(husk);
                        }
                        else {
                            HardHusk husk = new HardHusk(ModEntities.HARD_HUSK, world);
                            husk.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            husk.finalizeSpawn(world, world.getCurrentDifficultyAt(husk.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(husk);
                        }
                    }else if(entity.getType() == EntityType.ZOMBIFIED_PIGLIN){
                        //Summon ZPigman and set pos
                        if (world.random.nextInt(99) <= 4) {
                            HardZPiglinSwarmCaller pigZombie = new HardZPiglinSwarmCaller(ModEntities.HARD_Z_PIGLIN_SC, world);
                            pigZombie.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            pigZombie.finalizeSpawn(world, world.getCurrentDifficultyAt(pigZombie.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(pigZombie);
                        }else {
                            HardZPiglin pigZombie = new HardZPiglin(ModEntities.HARD_Z_PIGLIN, world);
                            pigZombie.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            pigZombie.finalizeSpawn(world, world.getCurrentDifficultyAt(pigZombie.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(pigZombie);
                        }

                    }else if(entity.getType() == EntityType.ZOMBIE_VILLAGER){
                        //not going to change them, because they are not true zombies
                        return;
                    }else if (entity.getType() == EntityType.DROWNED){
                        //Summon Drowned
                        if (world.random.nextInt(99) <= 4) {
                            HardDrownedSwarmCaller drowned = new HardDrownedSwarmCaller(ModEntities.HARD_DROWNED_SC, world);
                            drowned.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            drowned.finalizeSpawn(world, world.getCurrentDifficultyAt(drowned.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(drowned);
                        }else {
                            HardDrowned drowned = new HardDrowned(ModEntities.HARD_DROWNED, world);
                            drowned.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            drowned.finalizeSpawn(world, world.getCurrentDifficultyAt(drowned.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(drowned);
                        }
                    }
                    else {
                        //summon Zombie and set pos
                        if (world.random.nextInt(99) <= 4) {
                            HardZombieSwarmCaller zombie = new HardZombieSwarmCaller(ModEntities.HARD_ZOMBIE_SC, world);
                            zombie.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            zombie.finalizeSpawn(world, world.getCurrentDifficultyAt(zombie.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(zombie);
                        }else {
                            HardZombie zombie = new HardZombie(ModEntities.HARD_ZOMBIE, world);
                            zombie.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                            zombie.finalizeSpawn(world, world.getCurrentDifficultyAt(zombie.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            world.addFreshEntity(zombie);
                        }
                    }
                    event.setCanceled(true);
                }
                //If Skeleton
                else if(entity instanceof AbstractSkeletonEntity && !(entity instanceof HardSkeleton) && !(entity instanceof HardStray) && !(entity instanceof HardWitherSkeleton)){
                    if(entity instanceof StrayEntity){
                        //summon Stray and set pos and call on spawn initial func
                        HardStray stray = new HardStray(ModEntities.HARD_STRAY, world);
                        stray.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                        stray.finalizeSpawn(world, world.getCurrentDifficultyAt(stray.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(stray);
                    }
                    else if(entity instanceof WitherSkeletonEntity){
                        //summon WitherSkeleton and set pos and call on spawn initial func
                        HardWitherSkeleton witherSkeleton = new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, world);
                        witherSkeleton.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                        witherSkeleton.finalizeSpawn(world, world.getCurrentDifficultyAt(witherSkeleton.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(witherSkeleton);
                    }
                    else {
                        //summon Skeleton and set pos and call on spawn initial func
                        HardSkeleton skeleton = new HardSkeleton(ModEntities.HARD_SKELETON, world);
                        skeleton.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                        skeleton.finalizeSpawn(world, world.getCurrentDifficultyAt(skeleton.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(skeleton);
                    }
                    event.setCanceled(true);
                }
                //If Creeper
                else if (entity instanceof CreeperEntity && !(entity instanceof HardCreeper)){
                    //summon creeper and set pos
                    HardCreeper creeper = new HardCreeper(ModEntities.HARD_CREEPER, world);
                    creeper.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                    world.addFreshEntity(creeper);
                    event.setCanceled(true);
                }
                else if (entity instanceof SpiderEntity && !(entity instanceof HardSpider) && !(entity instanceof HardCaveSpider)){
                    if(entity instanceof CaveSpiderEntity){
                        HardCaveSpider caveSpider = new HardCaveSpider(ModEntities.HARD_CAVE_SPIDER, world);
                        caveSpider.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                        caveSpider.finalizeSpawn(world, world.getCurrentDifficultyAt(caveSpider.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(caveSpider);
                    }else {
                        HardSpider spider = new HardSpider(ModEntities.HARD_SPIDER, world);
                        spider.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                        spider.finalizeSpawn(world, world.getCurrentDifficultyAt(spider.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(spider);
                    }
                    event.setCanceled(true);
                }
                else if(entity instanceof EndermanEntity && !(entity instanceof HardEnderman)){
                    HardEnderman enderman = new HardEnderman(ModEntities.HARD_ENDERMAN, world);
                    enderman.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                    enderman.finalizeSpawn(world, world.getCurrentDifficultyAt(enderman.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addFreshEntity(enderman);
                    event.setCanceled(true);
                }
                else if(entity instanceof BlazeEntity && !(entity instanceof HardBlaze)){
                    HardBlaze blaze = new HardBlaze(ModEntities.HARD_BLAZE, world);
                    blaze.moveTo(X, Y, Z, entity.yRot, entity.xRot);
                    blaze.finalizeSpawn(world, world.getCurrentDifficultyAt(blaze.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addFreshEntity(blaze);
                    event.setCanceled(true);
                }
            }
        }
    }
}
