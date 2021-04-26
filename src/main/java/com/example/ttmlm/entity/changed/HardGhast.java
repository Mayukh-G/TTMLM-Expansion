package com.example.ttmlm.entity.changed;

import com.example.ttmlm.entity.ai.goal.GhastGoals;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HardGhast extends GhastEntity {
    public static final String name = "hard_ghast";
    public HardGhast(EntityType<? extends GhastEntity> type, World worldIn) {
        super(ModEntities.HARD_GHAST, worldIn);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.getDirectEntity() instanceof AbstractArrowEntity){
            return true;
        }else if(source.isExplosion() && source.getDirectEntity() instanceof HardGhast){
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
                new Predicate<LivingEntity>() { // Avoid Lambda references like these, They confuse the complied jar version
                    @Override
                    public boolean test(LivingEntity entity) {
                        return entity.getY() - HardGhast.super.getY() <= 4.0D;
                    }
                }
        ));
    }

    public static AttributeModifierMap.MutableAttribute createGhastAttributes() {
        return MonsterEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    public int explodePow(){
        return 2;
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
//    }


}
