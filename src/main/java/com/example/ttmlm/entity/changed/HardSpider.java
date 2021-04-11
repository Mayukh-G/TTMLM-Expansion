package com.example.ttmlm.entity.changed;

import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HardSpider extends SpiderEntity {
    public static final String name = "hard_spider";

    public HardSpider(EntityType<? extends SpiderEntity> type, World worldIn) {
        super(ModEntities.HARD_SPIDER, worldIn);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (worldIn.getRandom().nextInt(100) == 0) {
            HardSkeleton skeletonentity = new HardSkeleton(ModEntities.HARD_SKELETON, this.level);
            skeletonentity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            skeletonentity.finalizeSpawn(worldIn, difficultyIn, reason, (ILivingEntityData)null, (CompoundNBT)null);
            skeletonentity.startRiding(this);
        }
        if (spawnDataIn == null) {
            spawnDataIn = new SpiderEntity.GroupData();
            if (worldIn.getDifficulty() == Difficulty.HARD) {
                ((SpiderEntity.GroupData)spawnDataIn).setRandomEffect(worldIn.getRandom());
            }
        }

        if (spawnDataIn instanceof SpiderEntity.GroupData) {
            Effect effect = ((SpiderEntity.GroupData)spawnDataIn).effect;
            if (effect != null) {
                this.addEffect(new EffectInstance(effect, Integer.MAX_VALUE));
            }
        }

        return spawnDataIn;
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.36D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.36D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
//    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getMainHandItem().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)entityIn).addEffect(new EffectInstance(Effects.POISON, 20 * (int)f, 1));
        }
        return flag;
    }
}
