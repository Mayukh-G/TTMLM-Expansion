package com.example.ttmlm.entity.changed;

import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class HardCaveSpider extends CaveSpiderEntity {
    public static final String name = "hard_cave_spider";

    public HardCaveSpider(EntityType<? extends SpiderEntity> type, World worldIn) {
        super(ModEntities.HARD_CAVE_SPIDER, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createCaveSpiderAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
//    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getMainHandItem().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)entityIn).addEffect(new EffectInstance(Effects.POISON, 25 * (int)f, 1));
        }
        return flag;
    }
}
