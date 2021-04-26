package com.example.ttmlm.entity;

import com.example.ttmlm.config.CommonConfig;
import com.example.ttmlm.entity.changed.*;
import com.example.ttmlm.entity.original.HardDrownedSwarmCaller;
import com.example.ttmlm.entity.original.HardHuskSwarmCaller;
import com.example.ttmlm.entity.original.HardZPiglinSwarmCaller;
import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import com.example.ttmlm.init.ModEntities;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import javax.management.openmbean.ArrayType;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SpawnEntities {

    private static Set<Attribute> combatAttributes = ImmutableSet.of(Attributes.ATTACK_DAMAGE, Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.MOVEMENT_SPEED, Attributes.ATTACK_SPEED, Attributes.MAX_HEALTH);

    private static float getDropChance(MobEntity en){
        return en.getRandom().nextFloat() / 2;
    }

    private static <K extends MobEntity> void matchEntityProperties(MobEntity entityToMatch, K entityToMatchTo){
        double X = entityToMatch.getX();
        double Y = entityToMatch.getY();
        double Z = entityToMatch.getZ();
        // Rename entity to the custom name of the entity we are replacing
        ITextComponent customName = entityToMatch.getCustomName();

        // If no custom name do not replace
        if (customName != null) entityToMatchTo.setCustomName(customName);

        // Apply Effects
        for (EffectInstance instance : entityToMatch.getActiveEffects()) {
            entityToMatchTo.addEffect(instance);
        }

        // Apply attribute mods

        for (Attribute att: combatAttributes){
            if (entityToMatch.getAttributes().hasAttribute(att)) {
                ModifiableAttributeInstance a = entityToMatch.getAttribute(att);
                Set<AttributeModifier> modifiers = a.getModifiers();
                if (!modifiers.isEmpty()){
                    modifiers.forEach(attributeModifier -> {
                        AttributeModifier toApply = new AttributeModifier(UUID.randomUUID(), attributeModifier.getName(), attributeModifier.getAmount(), attributeModifier.getOperation());
                        entityToMatchTo.getAttribute(att).addPermanentModifier(toApply);
                    });
                    if (att == Attributes.MAX_HEALTH) {
                        entityToMatchTo.setHealth(entityToMatchTo.getMaxHealth());
                    }
                }
            }
        }

        // Armor slots, Hand Slots
        int randAlwaysDrop = entityToMatch.getRandom().nextInt(6);
        for (EquipmentSlotType slot: EquipmentSlotType.values()){
            ItemStack iStack = entityToMatch.getItemBySlot(slot);
            if (!iStack.isEmpty()){
                entityToMatchTo.setItemSlot(slot, iStack);
                if (slot.ordinal() == randAlwaysDrop) entityToMatchTo.setDropChance(slot, 1.0F);
                else entityToMatchTo.setDropChance(slot, SpawnEntities.getDropChance(entityToMatch));
            }
        }
        entityToMatchTo.moveTo(X, Y, Z, entityToMatch.yRot, entityToMatch.xRot);
    }

    public static void trySpawning(EntityJoinWorldEvent event){
        if (!CommonConfig.mobSwitch.get()) return; // Config Disables mod mobs when off
        if(!event.getWorld().isClientSide && (event.getEntity() instanceof MonsterEntity || event.getEntity() instanceof FlyingEntity)){
            Entity preEntity = event.getEntity();
            //Check is entity is inside chunk
            if (preEntity.inChunk){
                return;
            }
            ServerWorld world = (ServerWorld) event.getWorld();
            double X = preEntity.getX();
            double Y = preEntity.getY();
            double Z = preEntity.getZ();
            if(event.getEntity() instanceof FlyingEntity){
                FlyingEntity entity = (FlyingEntity) preEntity;
                if(entity instanceof GhastEntity && !(entity instanceof HardGhast)){
                    //Summon Ghast and set pos
                    HardGhast ghast = new HardGhast(ModEntities.HARD_GHAST, world);
                    SpawnEntities.matchEntityProperties(entity, ghast);
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
                            SpawnEntities.matchEntityProperties(entity, husk);
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
                            SpawnEntities.matchEntityProperties(entity, pigZombie);
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
                            SpawnEntities.matchEntityProperties(entity, drowned);
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
                            SpawnEntities.matchEntityProperties(entity, zombie);
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
                        SpawnEntities.matchEntityProperties(entity, stray);
                        stray.finalizeSpawn(world, world.getCurrentDifficultyAt(stray.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(stray);
                    }
                    else if(entity instanceof WitherSkeletonEntity){
                        //summon WitherSkeleton and set pos and call on spawn initial func
                        HardWitherSkeleton witherSkeleton = new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, world);
                        SpawnEntities.matchEntityProperties(entity, witherSkeleton);
                        witherSkeleton.finalizeSpawn(world, world.getCurrentDifficultyAt(witherSkeleton.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(witherSkeleton);
                    }
                    else {
                        //summon Skeleton and set pos and call on spawn initial func
                        HardSkeleton skeleton = new HardSkeleton(ModEntities.HARD_SKELETON, world);
                        SpawnEntities.matchEntityProperties(entity, skeleton);
                        skeleton.finalizeSpawn(world, world.getCurrentDifficultyAt(skeleton.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(skeleton);
                    }
                    event.setCanceled(true);
                }
                //If Creeper
                else if (entity instanceof CreeperEntity && !(entity instanceof HardCreeper)){
                    //summon creeper and set pos
                    HardCreeper creeper = new HardCreeper(ModEntities.HARD_CREEPER, world);
                    SpawnEntities.matchEntityProperties(entity, creeper);
                    world.addFreshEntity(creeper);
                    event.setCanceled(true);
                }
                else if (entity instanceof SpiderEntity && !(entity instanceof HardSpider) && !(entity instanceof HardCaveSpider)){
                    if(entity instanceof CaveSpiderEntity){
                        HardCaveSpider caveSpider = new HardCaveSpider(ModEntities.HARD_CAVE_SPIDER, world);
                        SpawnEntities.matchEntityProperties(entity, caveSpider);
                        caveSpider.finalizeSpawn(world, world.getCurrentDifficultyAt(caveSpider.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(caveSpider);
                    }else {
                        HardSpider spider = new HardSpider(ModEntities.HARD_SPIDER, world);
                        SpawnEntities.matchEntityProperties(entity, spider);
                        spider.finalizeSpawn(world, world.getCurrentDifficultyAt(spider.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        world.addFreshEntity(spider);
                    }
                    event.setCanceled(true);
                }
                else if(entity instanceof EndermanEntity && !(entity instanceof HardEnderman)){
                    HardEnderman enderman = new HardEnderman(ModEntities.HARD_ENDERMAN, world);
                    SpawnEntities.matchEntityProperties(entity, enderman);
                    enderman.finalizeSpawn(world, world.getCurrentDifficultyAt(enderman.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addFreshEntity(enderman);
                    event.setCanceled(true);
                }
                else if(entity instanceof BlazeEntity && !(entity instanceof HardBlaze)){
                    HardBlaze blaze = new HardBlaze(ModEntities.HARD_BLAZE, world);
                    SpawnEntities.matchEntityProperties(entity, blaze);
                    blaze.finalizeSpawn(world, world.getCurrentDifficultyAt(blaze.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                    world.addFreshEntity(blaze);
                    event.setCanceled(true);
                }
            }
        }
    }
}
