package com.example.examplemod.entity.changed;

import com.example.examplemod.entity.ai.goal.GhastGoals;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class HardGhast extends GhastEntity {
    public static final String name = "hard_ghast";
    public HardGhast(EntityType<? extends GhastEntity> type, World worldIn) {
        super(ModEntities.HARD_GHAST, worldIn);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.getImmediateSource() instanceof AbstractArrowEntity){
            return true;
        }else if(source.isExplosion() && source.getTrueSource() instanceof HardGhast){
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new GhastGoals.RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new GhastGoals.LookAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastGoals.FireballAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class,
                10,
                true,
                false,
                (p_213812_1_) -> Math.abs(p_213812_1_.getPosY() - this.getPosY()) <= 4.0D
        ));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
    }
}
