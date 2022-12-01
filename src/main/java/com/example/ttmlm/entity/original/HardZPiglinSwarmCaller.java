package com.example.ttmlm.entity.original;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.IAbstractHardZombie;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class HardZPiglinSwarmCaller extends ZombifiedPiglinEntity implements IAbstractHardZombie {
    public static final String name = "hard_zombie_piglin_sc";

    public HardZPiglinSwarmCaller(EntityType<?> type, World world) {
        super(ModEntities.HARD_Z_PIGLIN_SC, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(7, new HardZPiglinSwarmCaller.BuffAllies(this));
    }

    public static AttributeModifierMap.MutableAttribute createZPiglinSCAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 22.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.43D)
                .add(Attributes.ARMOR, 11.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(22.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.43D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(11.0D);
//    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected float getVoicePitch() {
        return 0.5F;
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return TTMLM.getID("entities/leader_zombie_type");
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.WEAK_BLAZING_AllOY.getSwordItem()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(IngotVariants.BLAZING_ALLOY.getHelmetItem()));
        this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;
        this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0;
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingBase) {
        if (livingBase instanceof NetherBoss){
            super.setTarget(null);
        }else {
            super.setTarget(livingBase);
        }
    }

    static class BuffAllies extends Goal{
        private HardZPiglinSwarmCaller swarmCaller;
        private int counter = 0;

        BuffAllies(HardZPiglinSwarmCaller caller){
            this.swarmCaller = caller;
        }

        @Override
        public boolean canUse() {
            return this.swarmCaller.getTarget() != null;
        }

        @Override
        public void tick() {
            if (this.counter >= 300){
                World world = this.swarmCaller.level;
                if(!world.isClientSide){
                    BlockPos minP = this.swarmCaller.blockPosition().north(10).east(10).below(5);
                    BlockPos maxP = this.swarmCaller.blockPosition().south(10).west(10).above(5);
                    AxisAlignedBB aabbP = new AxisAlignedBB(minP, maxP);
                    List<Entity> listZombie =  world.getEntities(this.swarmCaller, aabbP);
                    List<PlayerEntity> listPlayer = world.getEntitiesOfClass(PlayerEntity.class, aabbP);
                    if(!listZombie.isEmpty()){
                        EffectInstance zombieHeal = new EffectInstance(Effects.HARM, 100, 1);
                        for (Entity entity : listZombie) {
                            if (entity instanceof IAbstractHardZombie) {
                                ((LivingEntity) (entity)).addEffect(zombieHeal);
                            }
                        }
                    }
                    if(!listPlayer.isEmpty()){
                        EffectInstance playerWeak = new EffectInstance(Effects.CONFUSION, 170, 1);
                        EffectInstance playerSlow = new EffectInstance(Effects.WEAKNESS, 100);
                        for (PlayerEntity playerEntity : listPlayer) {
                            playerEntity.addEffect(playerWeak);
                            playerEntity.addEffect(playerSlow);
                        }
                    }
                    world.playSound(null, this.swarmCaller.blockPosition(), SoundEvents.ZOGLIN_DEATH, SoundCategory.HOSTILE, 2.5F, 0.5F);
                    this.counter = 0;
                }
            }
            this.counter++;
        }
    }
}
