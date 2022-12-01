package com.example.ttmlm.entity.changed;

import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class HardStray extends StrayEntity {
    public static final String name = "hard_stray";
    private final RangedBowAttackGoal<AbstractSkeletonEntity> aiArrow = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttack = new MeleeAttackGoal(this, 1.3D, false) {
        //Reset task when interrupted
        public void stop() {
            super.stop();
            HardStray.this.setAggressive(false);
        }

        //Start doing task
        public void start() {
            super.start();
            HardStray.this.setAggressive(true);
        }
    };

    public HardStray(EntityType<? extends StrayEntity> type, World world) {
        super(ModEntities.HARD_STRAY, world);
    }

    @Override
    public void reassessWeaponGoal() {
        if(!(this.aiArrow == null) && !(this.aiAttack == null)) {
            if (this.level != null && !this.level.isClientSide) {
                this.goalSelector.removeGoal(this.aiAttack);
                this.goalSelector.removeGoal(this.aiArrow);
                ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, Items.BOW));
                if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                    int i = 20;

                    this.aiArrow.setMinAttackInterval(i);
                    this.goalSelector.addGoal(4, this.aiArrow);
                } else {
                    this.goalSelector.addGoal(4, this.aiAttack);
                }
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level != null && !this.level.isClientSide) {
            ItemStack heldItem = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, Items.BOW));
            if (heldItem.getItem() == Items.BOW) {
                LivingEntity target = this.getTarget();
                if (target != null) {
                    double flatDist = this.distanceToSqr(target);
                    if (flatDist <= 8.0D){
                        this.goalSelector.removeGoal(this.aiArrow);
                        this.goalSelector.addGoal(4, this.aiAttack);
                    } else {
                        this.goalSelector.removeGoal(this.aiAttack);
                        this.goalSelector.addGoal(4, this.aiArrow);
                    }
                }
            }
        }
    }

    @Nonnull
    @Override
    protected AbstractArrowEntity getArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.getArrow(arrowStack, distanceFactor);
        if (abstractarrowentity instanceof ArrowEntity) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 600,1));
        }

        return abstractarrowentity;
    }

    public static AttributeModifierMap.MutableAttribute createStrayAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.5D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_KNOCKBACK, 4.0D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(4.0D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.5D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0f);
//    }

}
