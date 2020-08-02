package com.example.examplemod.entity.changed;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class HardHusk extends HuskEntity implements IAbstractHardZombie {
    public static final String name = "hard_husk";
    public boolean isLEADER;

    public HardHusk(EntityType<?> type, World world) {
        super(ModEntities.HARD_HUSK, world);
        this.isLEADER = rand.nextInt(99) <= 5;
    }

    @Override
    public void tick() {
        super.tick();
        TextComponent currentName = (TextComponent) this.getCustomName();
        if (currentName != this.leaderName && this.isLEADER){
            this.setCustomName(this.leaderName);
            this.setCustomNameVisible(true);
        }
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.2D);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && this.getHeldItemMainhand().isEmpty() && entityIn instanceof LivingEntity) {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.HUNGER, 140 * (int)f));
            ((LivingEntity)entityIn).addPotionEffect(new EffectInstance(Effects.NAUSEA, 70 * (int)f));
        }

        return flag;
    }

    @NotNull
    @Override
    protected ResourceLocation getLootTable() {
        if(this.isLEADER) {
            return ExampleMod.getID("entities/leader_zombie_type");
        }else {
            return super.getLootTable();
        }
    }

    @Override
    public void setLeaderAttributes() {
        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(this.getAttribute(SharedMonsterAttributes.ARMOR).getValue() * 6.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() + 0.025D);
    }
}


