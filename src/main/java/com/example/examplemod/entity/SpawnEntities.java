package com.example.examplemod.entity;

import com.example.examplemod.entity.changed.HardCreeper;
import com.example.examplemod.entity.changed.HardSkeleton;
import com.example.examplemod.entity.changed.HardZombie;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

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
            if(entity instanceof ZombieEntity && !(entity instanceof HardZombie)){
                if(entity.getType() == EntityType.HUSK){
                    return;
                }else if(entity.getType() == EntityType.ZOMBIE_PIGMAN){
                    return;
                }else if(entity.getType() == EntityType.ZOMBIE_VILLAGER){
                    return;
                }
                else {
                    //summon zombie and set pos
                    HardZombie zombie = new HardZombie(ModEntities.HARD_ZOMBIE, world);
                    zombie.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                    world.addEntity(zombie);
                    zombie.setPositionAndUpdate(X, Y, Z);
                }
                event.setCanceled(true);
            }
            //If Skeleton
            else if(entity instanceof SkeletonEntity && !(entity instanceof HardSkeleton)){
                //summon skeleton and set pos and call on spawn initial func
                HardSkeleton skeleton = new HardSkeleton(ModEntities.HARD_SKELETON, world);
                skeleton.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                world.addEntity(skeleton);
                skeleton.onSpawn(world, world.getDifficultyForLocation(skeleton.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                skeleton.setPositionAndUpdate(X, Y, Z);
                event.setCanceled(true);
            }
            //If Creeper
            else if (entity instanceof CreeperEntity && !(entity instanceof HardCreeper)){
                HardCreeper creeper = new HardCreeper(ModEntities.HARD_CREEPER, world);
                creeper.setPositionAndRotation(X, Y, Z, entity.rotationYaw, entity.rotationPitch);
                world.addEntity(creeper);
                event.setCanceled(true);
            }
        }
    }
}
