package com.example.ttmlm.entity.changed;

import com.example.ttmlm.entity.ai.goal.EndermanGoals;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class HardEnderman extends EndermanEntity {
    public static final String name = "hard_enderman";
    private static final Predicate<LivingEntity> field_213627_bA = (p_213626_0_) -> p_213626_0_ instanceof EndermiteEntity && ((EndermiteEntity)p_213626_0_).isPlayerSpawned();

    public HardEnderman(EntityType<? extends EndermanEntity> type, World worldIn) {
        super(ModEntities.HARD_ENDERMAN, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EndermanGoals.StareGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new EndermanGoals.PlaceBlockGoal(this));
        this.goalSelector.addGoal(11, new EndermanGoals.TakeBlockGoal(this));
        this.targetSelector.addGoal(1, new EndermanGoals.FindPlayerGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, EndermiteEntity.class, 10, true, false, field_213627_bA));
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 11.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33F);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(11.0D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
//    }

    public boolean isLookingAtMe(PlayerEntity player) {
        ItemStack itemstack = player.inventory.armor.get(3);
        if (itemstack.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            return false;
        } else {
            Vector3d vec3d = player.getViewVector(1.0F).normalize();
            Vector3d vec3d1 = new Vector3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
            double d0 = vec3d1.length();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dot(vec3d1);
            return d1 > 1.0D - 0.025D / d0 && player.canSee(this);
        }
    }

    @Override
    public boolean teleport() {
        return super.teleport();
    }

    public boolean teleportAtEntity(Entity p_70816_1_) {
        Vector3d vec3d = new Vector3d(this.getX() - p_70816_1_.getX(), this.getY(0.5D) - p_70816_1_.getEyeY(), this.getZ() - p_70816_1_.getZ());
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.x * d0;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vec3d.y * d0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.z * d0;
        return this.teleportHere(d1, d2, d3);
    }

    private boolean teleportHere(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.level.playSound((PlayerEntity)null, this.xOld, this.yOld, this.zOld, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
    }

}
