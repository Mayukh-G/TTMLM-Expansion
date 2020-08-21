package com.example.examplemod.entity.changed;

import com.example.examplemod.entity.original.NetherBoss;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HardBlaze extends BlazeEntity {
    public static final String name = "hard_blaze";

    public HardBlaze(EntityType<? extends BlazeEntity> type, World world) {
        super(ModEntities.HARD_BLAZE, world);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35.0D);
    }

    //make sure we dont retaliate against the boss
    @Override
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss){
            super.setRevengeTarget(null);
        }else {
            super.setRevengeTarget(livingBase);
        }
    }

    @Override
    public void livingTick() {
        LivingEntity target = this.getAttackTarget();
        if(target != null){
            if(this.getDistanceSq(target) < 3.0D){
                this.attackEntityAsMob(target);
            }
        }
        super.livingTick();
    }

}
