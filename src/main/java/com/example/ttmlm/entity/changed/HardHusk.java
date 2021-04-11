package com.example.ttmlm.entity.changed;

import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class HardHusk extends HuskEntity implements IAbstractHardZombie {
    public static final String name = "hard_husk";

    public HardHusk(EntityType<?> type, World world) {
        super(ModEntities.HARD_HUSK, world);
    }

    public static AttributeModifierMap.MutableAttribute createHuskAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
//        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.2D);
//    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag && this.getMainHandItem().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)entityIn).addEffect(new EffectInstance(Effects.HUNGER, 140 * (int)f));
            ((LivingEntity)entityIn).addEffect(new EffectInstance(Effects.CONFUSION, 70 * (int)f));
        }
        return flag;
    }

}


