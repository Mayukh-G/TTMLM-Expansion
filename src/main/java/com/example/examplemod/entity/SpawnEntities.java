package com.example.examplemod.entity;

import com.example.examplemod.entity.changed.*;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Random;

public class SpawnEntities {

    public static void trySpawning(EntityJoinWorldEvent event){
        if(!event.getWorld().isRemote() && event.getEntity() instanceof MonsterEntity){

            MonsterEntity entity = (MonsterEntity) event.getEntity(); //Check is entity is inside chunk
            if (entity.addedToChunk){
                return;
            }
            //.get() functions are slightly slow if used too much, minimising use by storing into variables
            World world = event.getWorld();
            double X = entity.getPosX();
            double Y = entity.getPosY();
            double Z = entity.getPosZ();
            //If zombie
            if(entity instanceof ZombieEntity && !(entity instanceof IAbstractHardZombie)){

                Random rand = new Random(); //ADDING LEADER ZOMBIES
                boolean leader = rand.nextInt(99) <= 5; //ADD EXTRA REWARDS FOR LEADERS

                if(entity.getType() == EntityType.HUSK){
                    //Summon Husk and set pos
                    HardHusk husk = new HardHusk(ModEntities.HARD_HUSK, world);
                    husk.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    husk.onInitialSpawn(world, world.getDifficultyForLocation(husk.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    if(leader){
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
                    if(leader){
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
                    if(leader){
                        drowned.setLeaderAttributes();
                        drowned.setCustomName(drowned.leaderName);
                        drowned.setCustomNameVisible(true);
                    }
                    world.addEntity(drowned);
                }
                else {
                    //summon Zombie and set pos
                    HardZombie zombie = new HardZombie(ModEntities.HARD_ZOMBIE, world);
                    zombie.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    zombie.onInitialSpawn(world, world.getDifficultyForLocation(zombie.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    if(leader){
                        zombie.setLeaderAttributes();
                        zombie.setCustomName(zombie.leaderName);
                        zombie.setCustomNameVisible(true);
                    }
                    world.addEntity(zombie);
                }
                event.setCanceled(true);
            }
            //If Skeleton
            else if(entity instanceof SkeletonEntity && !(entity instanceof HardSkeleton)){
                //summon Skeleton and set pos and call on spawn initial func
                HardSkeleton skeleton = new HardSkeleton(ModEntities.HARD_SKELETON, world);
                skeleton.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                world.addEntity(skeleton);
                skeleton.onSpawn(world, world.getDifficultyForLocation(skeleton.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                event.setCanceled(true);
            }
            //If Creeper
            else if (entity instanceof CreeperEntity && !(entity instanceof HardCreeper)){
                //summon Skeleton and set pos
                HardCreeper creeper = new HardCreeper(ModEntities.HARD_CREEPER, world);
                creeper.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                world.addEntity(creeper);
                event.setCanceled(true);
            }
        }
    }
}
