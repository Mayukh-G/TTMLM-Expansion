package com.example.ttmlm.entity.changed;

import com.example.ttmlm.entity.original.NetherBoss;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HardBlaze extends BlazeEntity {
    public static final String name = "hard_blaze";

    public HardBlaze(EntityType<? extends BlazeEntity> type, World world) {
        super(ModEntities.HARD_BLAZE, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes(){
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0D)
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(3.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
//    }

    //Make sure we don't retaliate against the boss
    @Override
    public void setTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss){
            super.setTarget(null);
        }else {
            super.setTarget(livingBase);
        }
    }

    @Override
    public void baseTick() {
        LivingEntity target = this.getTarget();
        if(target != null){
            if(this.distanceToSqr(target) < 3.0D){
                this.doHurtTarget(target);
            }
        }
        super.baseTick();
    }



}
