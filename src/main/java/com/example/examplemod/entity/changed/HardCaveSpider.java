package com.example.examplemod.entity.changed;

import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class HardCaveSpider extends CaveSpiderEntity {
    public static final String name = "hard_cave_spider";

    public HardCaveSpider(EntityType<? extends SpiderEntity> type, World worldIn) {
        super(ModEntities.HARD_CAVE_SPIDER, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getHeldItemMainhand().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 25 * (int)f, 1));
        }
        return flag;
    }
}
