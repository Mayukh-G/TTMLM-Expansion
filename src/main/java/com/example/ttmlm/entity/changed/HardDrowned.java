package com.example.ttmlm.entity.changed;

import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

public class HardDrowned extends DrownedEntity implements IAbstractHardZombie {
    public static final String name = "hard_drowned";

    public HardDrowned(EntityType<? extends DrownedEntity> type, World worldIn) {
        super(ModEntities.HARD_DROWNED, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createDrownedAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2D);
    }

//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
//        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.2D);
//        this.getAttribute(SWIM_SPEED).setBaseValue(2.0D);
//    }

//    @Override
//    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
//        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
//    }
//
//    @Override
//    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
//        super.populateDefaultEquipmentSlots(p_180481_1_);
//    }

}
