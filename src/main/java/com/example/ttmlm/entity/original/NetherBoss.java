package com.example.ttmlm.entity.original;

import com.example.ttmlm.entity.changed.HardBlaze;
import com.example.ttmlm.entity.changed.HardWitherSkeleton;
import com.example.ttmlm.entity.changed.HardZPiglin;
import com.example.ttmlm.init.IngotVariants;
import com.example.ttmlm.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class NetherBoss extends MonsterEntity implements IRangedAttackMob {
    /* TODO : Obfuscated makes the text do the cycle thing, Make it so it stays like that and the Boss doesnt take damage until
        The player goes to a the custom dimension and does an event */

            public static final String name = "nether_boss";
            private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(bossName, BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
            private int shotCount = 0;
            private int coolDown = 0;
            // Bold, Obfuscated, Dark Red
            private static final IFormattableTextComponent bossName = new StringTextComponent("THE CHALLENGER")
                    .withStyle(TextFormatting.BOLD, TextFormatting.OBFUSCATED, TextFormatting.DARK_RED);

    public NetherBoss(EntityType<? extends NetherBoss> type, World levelIn) {
        super(type, levelIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new NetherBoss.NetherBossExplode(this));
        this.goalSelector.addGoal(8, new NetherBoss.SummonHoardGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 5, 5,10.0F){
            @Override
            public void stop() {
                super.stop();
                NetherBoss.this.setAggressive(false);
            }

            @Override
            public void start() {
                super.start();
                NetherBoss.this.setAggressive(true);
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, true, null));
    }



//    @Override
//    public boolean isNonBoss() {
//        return false;
//    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(this.getHealth() <= 298 && this.tickCount%20 == 0){
            if(!this.level.isClientSide){
                this.heal(1.5F);
            }
        }
    }

    @Override
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.remove();
        } else {
            this.noActionTime = 0;
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void makeStuckInBlock(BlockState p_213295_1_, @NotNull Vector3d p_213295_2_) {
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOGLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    @NotNull
    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    public boolean addEffect(@NotNull EffectInstance effectInstance) {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemEntity itemEntity1;
        ItemEntity itemEntity2;
        if(this.random.nextBoolean()) {
           itemEntity1 = this.spawnAtLocation(IngotVariants.BLAZING_ALLOY.getIngotBlock());
           itemEntity2 = this.spawnAtLocation(Items.GOLD_BLOCK);
        }else {
            itemEntity1 = this.spawnAtLocation(IngotVariants.BLAZING_ALLOY.getChestplateItem());
            itemEntity2 = this.spawnAtLocation(IngotVariants.BLAZING_ALLOY.getBootsItem());
        }
        if(itemEntity1 != null && itemEntity2 != null){
            itemEntity1.setExtendedLifetime();
            itemEntity2.setExtendedLifetime();
        }
    }

    public static AttributeModifierMap.MutableAttribute createNetherBossAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.6D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0);
    }

//    @Override
//    protected void registerAttributes() {
//        super.registerAttributes();
//        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(600.0D);
//        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
//        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
//        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
//    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        if(this.shotCount < 10) {
            if(!this.level.isClientSide) {
                Vector3d increaseAcc = target.getDeltaMovement();
                double accelX = (target.getX() - this.getX()) + increaseAcc.x * 5;
                double accelY = target.getY(0.5D) - this.getY(0.5D) + increaseAcc.y;
                double accelZ = target.getZ() - this.getZ() + increaseAcc.z * 5;
                double fx = this.getX();
                double fy = this.getY(0.5D) + 0.5D;
                double fz = this.getZ();
                if(this.shotCount < 9){
                    SmallFireballEntity smallFireball = new SmallFireballEntity(this.level, this, accelX * 2, accelY * 2, accelZ * 2);
                    smallFireball.setPos(fx , fy, fz );
                    this.level.playSound(null, this.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    this.level.addFreshEntity(smallFireball);
                }else {
                    FireballEntity fireball = new FireballEntity(this.level, this, accelX * 2, accelY * 2, accelZ * 2);
                    fireball.setPos(fx , fy, fz );
                    fireball.explosionPower = 2;
                    this.level.playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    this.level.addFreshEntity(fireball);
                }
                this.shotCount++;
            }
        }
        else if(this.coolDown >= 15){
            this.shotCount = 0;
            this.coolDown = 0;
        }
        else {
            this.coolDown++;
        }
    }

    static class NetherBossExplode extends Goal{
        private NetherBoss boss;
        private int counter;

        public NetherBossExplode(NetherBoss bossIn){
            this.boss = bossIn;
        }

        @Override
        public boolean canUse() {
            if(this.boss.isAggressive() && this.boss.getTarget() != null){
                return this.boss.getHealth() <= this.boss.getMaxHealth()*2/3;
            }
            return false;
        }

        @Override
        public void start() {
            this.counter = 0;
        }

        @Override
        public void tick() {
            if(this.counter >= 150){
                int maxAttack = 5;
                int angle = 90/maxAttack;
                LivingEntity target = this.boss.getTarget();
                Vector3d[] vecRotations = new Vector3d[maxAttack];
                FireballEntity[] fireballs = new FireballEntity[maxAttack];
                double posX = this.boss.getX();
                double posZ = this.boss.getZ();
                double baseAccelY = target.getY(0.5D) - (0.5D + this.boss.getY(0.5D));

                this.boss.level.playSound(null, this.boss.blockPosition(), SoundEvents.WITHER_DEATH, SoundCategory.HOSTILE, 1.0F, 1.0F);
                for(int i = 1; i < maxAttack+1; i++) {
                    vecRotations[i-1] = Vector3d.directionFromRotation(0, this.boss.yHeadRot - 45 + angle*i);
                    fireballs[i-1] = new FireballEntity(this.boss.level, this.boss,
                            target.getX() - (posX + vecRotations[i-1].x * 5.0D),
                            baseAccelY,
                            target.getZ() - (posZ + vecRotations[i-1].z * 5.0D));
                    fireballs[i-1].explosionPower = 3;
                    fireballs[i-1].setPos(posX + this.boss.random.nextGaussian(), this.boss.getY(0.5D) + 0.5D, posZ + this.boss.random.nextGaussian());

                    this.boss.level.addFreshEntity(fireballs[i-1]);
                }

                counter = 0;
            }
            counter++;
        }
    }

    static class SummonHoardGoal extends Goal{
        private NetherBoss boss;
        private int counter;

        SummonHoardGoal(NetherBoss bossIn){
            this.boss = bossIn;
        }

        @Override
        public boolean canUse() {
            if(this.boss.isAggressive() && this.boss.getTarget() != null){
                return this.boss.getHealth() <= this.boss.getMaxHealth() / 2;
            }
            return false;
        }

        @Override
        public void start() {
            this.counter = 0;
        }

        @Override
        public void tick(){
            if(this.counter >= 350){
                int typeNum = this.boss.random.nextInt(10);
                int counterIn = 0;
                boolean maxAttempt = false;
                LivingEntity target = this.boss.getTarget();
                BlockPos blockPos;
                ServerWorld world = (ServerWorld) this.boss.level;
                double posX;
                double posZ;
                double posY;
                Random random = this.boss.random;
                if(typeNum <= 3){ // <= 3
                    HardZPiglin[] pigMen = new HardZPiglin[]{
                            new HardZPiglin(ModEntities.HARD_Z_PIGLIN, this.boss.level),
                            new HardZPiglin(ModEntities.HARD_Z_PIGLIN, this.boss.level),
                            new HardZPiglin(ModEntities.HARD_Z_PIGLIN, this.boss.level)};
                    for (int i = 0; i < 3; i++){
                        do {
                            posX = this.boss.getX() + random.nextDouble() * 2;
                            posZ = this.boss.getZ() + random.nextDouble() * 2;
                            posY = this.boss.getY();
                            blockPos = new BlockPos(posX, posY, posZ);
                            pigMen[i].setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            if(counterIn >= 30){
                                maxAttempt = true;
                            }
                            counterIn++;
                        }while (isNotSpawnable(pigMen[i]) && !maxAttempt);
                        if(!maxAttempt){
                            pigMen[i].finalizeSpawn(world, this.boss.level.getCurrentDifficultyAt(blockPos), SpawnReason.EVENT, null, null );
                            pigMen[i].setTarget(target);
                            this.boss.level.playSound(null, this.boss.blockPosition(), SoundEvents.ZOGLIN_ATTACK, SoundCategory.HOSTILE, 1.0F, 1.0F);
                            this.boss.level.addFreshEntity(pigMen[i]);
                        }
                    }

                }else if(typeNum <= 6){ // <= 6
                    HardWitherSkeleton[] witherSkeletons = new HardWitherSkeleton[]{
                            new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, this.boss.level),
                            new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, this.boss.level)
                    };
                    for (int i = 0; i < 2; i++) {
                        do {
                            posX = this.boss.getX() + random.nextDouble() * 4;
                            posZ = this.boss.getZ() + random.nextDouble() * 4;
                            posY = this.boss.getY();
                            blockPos = new BlockPos(posX, posY, posZ);
                            witherSkeletons[i].setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            if (counterIn >= 30) {
                                maxAttempt = true;
                            }
                            counterIn++;
                        } while (isNotSpawnable(witherSkeletons[i]) && !maxAttempt);
                        if(!maxAttempt){
                            witherSkeletons[i].finalizeSpawn(world, this.boss.level.getCurrentDifficultyAt(blockPos), SpawnReason.EVENT, null, null );
                            witherSkeletons[i].setTarget(target);
                            this.boss.level.playSound(null, this.boss.blockPosition(), SoundEvents.ZOGLIN_ATTACK, SoundCategory.HOSTILE, 1.0F, 1.0F);
                            this.boss.level.addFreshEntity(witherSkeletons[i]);
                        }
                    }

                }else {
                    HardBlaze blaze = new HardBlaze(ModEntities.HARD_BLAZE, this.boss.level);
                    do {
                        posX = this.boss.getX() + random.nextDouble() * 4;
                        posZ = this.boss.getZ() + random.nextDouble() * 4;
                        posY = this.boss.getY();
                        blockPos = new BlockPos(posX, posY, posZ);
                        blaze.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        if (counterIn >= 30) {
                            maxAttempt = true;
                        }
                        counterIn++;
                    } while (isNotSpawnable(blaze) && !maxAttempt);
                    if(!maxAttempt){
                        blaze.finalizeSpawn(world, this.boss.level.getCurrentDifficultyAt(blockPos), SpawnReason.EVENT, null, null );
                        blaze.setTarget(target);
                        this.boss.level.playSound(null, this.boss.blockPosition(), SoundEvents.ZOGLIN_ATTACK, SoundCategory.HOSTILE, 1.0F, 1.0F);
                        this.boss.level.addFreshEntity(blaze);
                    }
                }
                this.counter = 0;
            }
            this.counter++;
        }

        private boolean isNotSpawnable(MonsterEntity monsterEntity){
            return  !this.boss.level.noCollision(monsterEntity)
                    && this.boss.level.containsAnyLiquid(monsterEntity.getBoundingBox())
                    && !this.boss.level.isClientSide;
        }
    }
}
