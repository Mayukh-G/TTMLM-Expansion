package com.example.ttmlm.entity.original;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.entity.changed.IAbstractHardZombie;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.List;

public class HardDrownedSwarmCaller extends DrownedEntity implements IAbstractHardZombie {
    public static final String name = "hard_drowned_sc";

    public HardDrownedSwarmCaller(EntityType<?> type, World world) {
        super(ModEntities.HARD_DROWNED_SC, world);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(7, new HardDrownedSwarmCaller.BuffAllies(this));
    }

    public static AttributeModifierMap.MutableAttribute createDrownedSCAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 22.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ARMOR, 11.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.4D);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(22.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40D);
//        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(11.0D);
//        this.getAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.4D);
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
        if ((double) this.random.nextFloat() > 0.9D) {
            int i = this.random.nextInt(16);
            if (i < 10) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.TRIDENT));
            } else {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }
    }

    static class BuffAllies extends Goal{
        private HardDrownedSwarmCaller swarmCaller;
        private int counter = 0;

        BuffAllies(HardDrownedSwarmCaller caller){
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
                        EffectInstance playerWeak = new EffectInstance(Effects.DIG_SLOWDOWN, 170);
                        EffectInstance playerSlow = new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1);
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
