package com.example.examplemod.entity.changed;

import com.example.examplemod.entity.original.NetherBoss;
import com.example.examplemod.init.IngotVariants;
import com.example.examplemod.init.ModEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
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
        public void resetTask() {
            super.resetTask();
            HardWitherSkeleton.this.setAggroed(false);
        }

        //Start doing task
        public void startExecuting() {
            super.startExecuting();
            HardWitherSkeleton.this.setAggroed(true);
        }
    };

    public HardWitherSkeleton(EntityType<? extends WitherSkeletonEntity> typeIn, World worldIn) {
        super(ModEntities.HARD_WITHER_SKELETON, worldIn);
    }

    @Override
    public void setCombatTask() {
        if(!(this.aiArrow == null) && !(this.aiAttack == null)) {
            if (this.world != null && !this.world.isRemote) {
                this.goalSelector.removeGoal(this.aiAttack);
                this.goalSelector.removeGoal(this.aiArrow);
                ItemStack itemstack = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW));
                if (itemstack.getItem() instanceof net.minecraft.item.BowItem) {
                    int i = 17;
                    if (this.world.getDifficulty() != Difficulty.HARD) {
                        i = 20;
                    }

                    this.aiArrow.setAttackCooldown(i);
                    this.goalSelector.addGoal(4, this.aiArrow);
                } else {
                    this.goalSelector.addGoal(4, this.aiAttack);
                }
            }
        }
    }

    //make sure we dont retaliate against the boss
    @Override
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss || livingBase instanceof HardWitherSkeleton){
            super.setRevengeTarget(null);
        }else {
            super.setRevengeTarget(livingBase);
        }
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if(this.rand.nextInt(9) <= 4){
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.BLAZING_ALLOY.getSwordItem()));
            this.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(IngotVariants.BLAZING_ALLOY.getChestplateItem()));
            this.inventoryArmorDropChances[EquipmentSlotType.CHEST.getIndex()] = 0;
        }else {
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
            this.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(IngotVariants.BLAZING_ALLOY.getLegginsItem()));
            this.inventoryArmorDropChances[EquipmentSlotType.LEGS.getIndex()] = 0;
        }
        this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;
    }

    @Override
    protected void setEnchantmentBasedOnDifficulty(@NotNull DifficultyInstance difficulty) {
        float f = difficulty.getClampedAdditionalDifficulty();
        for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getSlotType() == EquipmentSlotType.Group.ARMOR) {
                ItemStack itemstack = this.getItemStackFromSlot(equipmentslottype);
                if (!itemstack.isEmpty() && this.rand.nextFloat() < 0.5F * f) {
                    this.setItemStackToSlot(equipmentslottype, EnchantmentHelper.addRandomEnchantment(this.rand, itemstack, (int)(5.0F + f * (float)this.rand.nextInt(18)), false));
                }
            }
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.world != null && !this.world.isRemote) {
            ItemStack heldItem = this.getHeldItem(ProjectileHelper.getHandWith(this, Items.BOW));
            if (heldItem.getItem() == Items.BOW) {
                LivingEntity target = this.getAttackTarget();
                if (target != null) {
                    double flatDist = this.getDistanceSq(target);
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

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(4.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    @NotNull
    @Override
    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.fireArrow(arrowStack, distanceFactor);
        abstractarrowentity.setFire(100);
        if (abstractarrowentity instanceof ArrowEntity) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.WITHER, 200));
        }
        return abstractarrowentity;
    }

}
