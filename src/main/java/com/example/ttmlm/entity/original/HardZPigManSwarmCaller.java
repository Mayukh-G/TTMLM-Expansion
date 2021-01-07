package com.example.ttmlm.entity.original;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.HardZombie;
import com.example.ttmlm.entity.changed.IAbstractHardZombie;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class HardZPigManSwarmCaller extends ZombiePigmanEntity implements IAbstractHardZombie {
    public static final String name = "hard_zombie_pigman_sc";

    public HardZPigManSwarmCaller(EntityType<?> type, World world) {
        super(ModEntities.HARD_Z_PIGMAN_SC, world);
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.goalSelector.addGoal(7, new HardZPigManSwarmCaller.BuffAllies(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.43D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(11.0D);
    }

    @Override
    protected boolean shouldDrown() {
        return false;
    }

    @Override
    protected float getSoundPitch() {
        return 0.5F;
    }

    @NotNull
    @Override
    protected ResourceLocation getLootTable() {
        return TTMLM.getID("entities/leader_zombie_type");
    }

    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(@NotNull DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.WEAK_BLAZING_AllOY.getSwordItem()));
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(IngotVariants.BLAZING_ALLOY.getHelmetItem()));
        this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;
        this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0;
    }

    @Override
    public void setRevengeTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss){
            super.setRevengeTarget(null);
        }else {
            super.setRevengeTarget(livingBase);
        }
    }

    static class BuffAllies extends Goal{
        private HardZPigManSwarmCaller swarmCaller;
        private int counter = 0;

        BuffAllies(HardZPigManSwarmCaller caller){
            this.swarmCaller = caller;
        }

        @Override
        public boolean shouldExecute() {
            return this.swarmCaller.getAttackTarget() != null;
        }


        @Override
        public void tick() {
            if (this.counter >= 300){
                World world = this.swarmCaller.world;
                if(!world.isRemote){
                    BlockPos minP = this.swarmCaller.getPosition().north(10).east(10).down(5);
                    BlockPos maxP = this.swarmCaller.getPosition().south(10).west(10).up(5);
                    AxisAlignedBB aabbP = new AxisAlignedBB(minP, maxP);
                    List<Entity> listZombie =  world.getEntitiesWithinAABBExcludingEntity(this.swarmCaller, aabbP);
                    List<PlayerEntity> listPlayer = world.getEntitiesWithinAABB(PlayerEntity.class, aabbP);
                    if(!listZombie.isEmpty()){
                        EffectInstance zombieHeal = new EffectInstance(Effects.INSTANT_DAMAGE, 100, 1);
                        for (Entity entity : listZombie) {
                            if (entity instanceof IAbstractHardZombie) {
                                ((LivingEntity) (entity)).addPotionEffect(zombieHeal);
                            }
                        }
                    }
                    if(!listPlayer.isEmpty()){
                        EffectInstance playerWeak = new EffectInstance(Effects.NAUSEA, 170, 1);
                        EffectInstance playerSlow = new EffectInstance(Effects.WEAKNESS, 100);
                        for (PlayerEntity playerEntity : listPlayer) {
                            playerEntity.addPotionEffect(playerWeak);
                            playerEntity.addPotionEffect(playerSlow);
                        }
                    }
                    world.playSound(null, this.swarmCaller.getPosition(), SoundEvents.ENTITY_ZOMBIE_PIGMAN_DEATH, SoundCategory.HOSTILE, 2.5F, 0.5F);
                    this.counter = 0;
                }
            }
            this.counter++;
        }
    }
}
