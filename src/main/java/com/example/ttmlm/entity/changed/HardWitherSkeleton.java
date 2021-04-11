package com.example.ttmlm.entity.changed;

import com.example.ttmlm.entity.original.NetherBoss;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HardWitherSkeleton extends WitherSkeletonEntity {
    public static final String name = "hard_wither_skeleton";
    private final RangedBowAttackGoal<AbstractSkeletonEntity> aiArrow = new RangedBowAttackGoal<>(this, 1.0D, 17, 20.0F);
    private final MeleeAttackGoal aiAttack = new MeleeAttackGoal(this, 1.3D, false) {
        //Reset task when interrupted
        public void stop() {
            super.stop();
            HardWitherSkeleton.this.setAggressive(false);
        }

        //Start doing task
        public void start() {
            super.start();
            HardWitherSkeleton.this.setAggressive(true);
        }
    };

    public HardWitherSkeleton(EntityType<? extends WitherSkeletonEntity> typeIn, World worldIn) {
        super(ModEntities.HARD_WITHER_SKELETON, worldIn);
    }

    @Override
    public void reassessWeaponGoal() {
        if(!(this.aiArrow == null) && !(this.aiAttack == null)) {
            if (this.level != null && !this.level.isClientSide) {
                this.goalSelector.removeGoal(this.aiAttack);
                this.goalSelector.removeGoal(this.aiArrow);
                ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, Items.BOW));
                if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                    int i = 17;
                    if (this.level.getDifficulty() != Difficulty.HARD) {
                        i = 20;
                    }

                    this.aiArrow.setMinAttackInterval(i);
                    this.goalSelector.addGoal(4, this.aiArrow);
                } else {
                    this.goalSelector.addGoal(4, this.aiAttack);
                }
            }
        }
    }

    //Make sure we don't retaliate against the boss
    @Override
    public void setTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss || livingBase instanceof HardWitherSkeleton){
            super.setTarget(null);
        }else {
            super.setTarget(livingBase);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if(this.random.nextBoolean()){
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.BLAZING_ALLOY.getSwordItem()));
            this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(IngotVariants.BLAZING_ALLOY.getChestplateItem()));
            this.armorDropChances[EquipmentSlotType.CHEST.getIndex()] = 0;
        }else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
            this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(IngotVariants.BLAZING_ALLOY.getLegginsItem()));
            this.armorDropChances[EquipmentSlotType.LEGS.getIndex()] = 0;
        }
        this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;

    }

    @Override
    protected void populateDefaultEquipmentEnchantments(@NotNull DifficultyInstance difficulty) {
        float f = difficulty.getEffectiveDifficulty();
        for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
                ItemStack itemstack = this.getItemBySlot(equipmentslottype);
                if (!itemstack.isEmpty() && this.random.nextFloat() < 0.5F * f) {
                    this.setItemSlot(equipmentslottype, EnchantmentHelper.enchantItem(this.random, itemstack, (int)(5.0F + f * (float)this.random.nextInt(18)), false));
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

    public static AttributeModifierMap.MutableAttribute createWSkeletonAttributes() {
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
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
//    }

    @NotNull
    @Override
    protected AbstractArrowEntity getArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.getArrow(arrowStack, distanceFactor);
        abstractarrowentity.setSecondsOnFire(100);
        if (abstractarrowentity instanceof ArrowEntity) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.WITHER, 200));
        }
        return abstractarrowentity;
    }

}
