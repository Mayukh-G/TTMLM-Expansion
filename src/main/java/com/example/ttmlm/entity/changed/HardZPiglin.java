package com.example.ttmlm.entity.changed;

import com.example.ttmlm.entity.original.NetherBoss;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HardZPiglin extends ZombifiedPiglinEntity implements IAbstractHardZombie {
    public static final String name = "hard_zombie_piglin";

    public HardZPiglin(EntityType<?> type, World world) {
        super(ModEntities.HARD_Z_PIGLIN, world);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.WEAK_BLAZING_AllOY.getSwordItem()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(IngotVariants.BLAZING_ALLOY.getHelmetItem()));
        this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;
        this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0;
    }

    public static AttributeModifierMap.MutableAttribute createZpPiglinAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.40D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
//    }

    //make sure we dont retaliate against the boss
    @Override
    public void setTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss){
            super.setTarget(null);
        }else {
            super.setTarget(livingBase);
        }
    }
}
