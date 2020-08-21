package com.example.examplemod.entity.original;

import com.example.examplemod.entity.changed.HardBlaze;
import com.example.examplemod.entity.changed.HardWitherSkeleton;
import com.example.examplemod.entity.changed.HardZPigMan;
import com.example.examplemod.init.IngotVariants;
import com.example.examplemod.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class NetherBoss extends MonsterEntity implements IRangedAttackMob { //Fix the boss room so it doesnt destroy itself
            public static final String name = "nether_boss";
            private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(bossName, BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
            private int shotCount = 0;
            private int coolDown = 0;
            private static final TextComponent bossName = new TextComponent() {
                @Override
                @Nonnull
                public String getUnformattedComponentText() {
                    return "\u00A7l\u00A7k\u00A74THE CHALLENGER";
                }

                @Override
                @Nonnull
                public ITextComponent shallowCopy() {
                    return this;
                }


            };

    public NetherBoss(EntityType<? extends NetherBoss> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new NetherBoss.NetherBossExplode(this));
        this.goalSelector.addGoal(8, new NetherBoss.SummonHoardGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 5, 5,10.0F){
            @Override
            public void resetTask() {
                super.resetTask();
                NetherBoss.this.setAggroed(false);
            }

            @Override
            public void startExecuting() {
                super.startExecuting();
                NetherBoss.this.setAggroed(true);
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, false, true, null));
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(this.getHealth() <= 298 && this.ticksExisted%20 == 0){
            if(!this.world.isRemote){
                this.heal(1.5F);
            }
        }
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
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
        return SoundEvents.ENTITY_ZOMBIE_PIGMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_PIGMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH;
    }

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropSpecialItems(source, looting, recentlyHitIn);
        ItemEntity itemEntity1;
        ItemEntity itemEntity2;
        if(this.rand.nextBoolean()) {
           itemEntity1 = this.entityDropItem(IngotVariants.BLAZING_ALLOY.getIngotBlock());
           itemEntity2 = this.entityDropItem(Items.GOLD_BLOCK);
        }else {
            itemEntity1 = this.entityDropItem(IngotVariants.BLAZING_ALLOY.getChestplateItem());
            itemEntity2 = this.entityDropItem(IngotVariants.BLAZING_ALLOY.getBootsItem());
        }
        if(itemEntity1 != null && itemEntity2 != null){
            itemEntity1.setNoDespawn();
            itemEntity2.setNoDespawn();
        }
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(600.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

    @Override
    public void attackEntityWithRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        if(this.shotCount < 10) {
            if(!this.world.isRemote) {
                Vec3d increaseAcc = target.getMotion();
                double accelX = (target.getPosX() - this.getPosX()) + increaseAcc.x * 5;
                double accelY = target.getPosYHeight(0.5D) - this.getPosYHeight(0.5D) + increaseAcc.y;
                double accelZ = target.getPosZ() - this.getPosZ() + increaseAcc.z * 5;
                double fx = this.getPosX();
                double fy = this.getPosYHeight(0.5D) + 0.5D;
                double fz = this.getPosZ();
                if(this.shotCount < 9){
                    SmallFireballEntity smallFireball = new SmallFireballEntity(this.world, this, accelX * 2, accelY * 2, accelZ * 2);
                    smallFireball.setPosition(fx , fy, fz );
                    this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    this.world.addEntity(smallFireball);
                }else {
                    FireballEntity fireball = new FireballEntity(this.world, this, accelX * 2, accelY * 2, accelZ * 2);
                    fireball.setPosition(fx , fy, fz );
                    fireball.explosionPower = 2;
                    this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                    this.world.addEntity(fireball);
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
        public boolean shouldExecute() {
            if(this.boss.isAggressive() && this.boss.getAttackTarget() != null){
                return this.boss.getHealth() <= this.boss.getMaxHealth()*2/3;
            }
            return false;
        }

        @Override
        public void startExecuting() {
            this.counter = 0;
        }

        @Override
        public void tick() {
            if(this.counter >= 150){
                int maxAttack = 5;
                int angle = 90/maxAttack;
                LivingEntity target = this.boss.getAttackTarget();
                Vec3d[] vecRotations = new Vec3d[maxAttack];
                FireballEntity[] fireballs = new FireballEntity[maxAttack];
                double posX = this.boss.getPosX();
                double posZ = this.boss.getPosZ();
                double baseAccelY = target.getPosYHeight(0.5D) - (0.5D + this.boss.getPosYHeight(0.5D));

                this.boss.world.playSound(null, this.boss.getPosition(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.HOSTILE, 1.0F, 1.0F);
                for(int i = 1; i < maxAttack+1; i++) {
                    vecRotations[i-1] = Vec3d.fromPitchYaw(0, this.boss.getRotationYawHead() - 45 + angle*i);
                    fireballs[i-1] = new FireballEntity(this.boss.world, this.boss,
                            target.getPosX() - (posX + vecRotations[i-1].x * 5.0D),
                            baseAccelY,
                            target.getPosZ() - (posZ + vecRotations[i-1].z * 5.0D));
                    fireballs[i-1].explosionPower = 3;
                    fireballs[i-1].setPosition(posX + this.boss.rand.nextGaussian(), this.boss.getPosYHeight(0.5D) + 0.5D, posZ + this.boss.rand.nextGaussian());

                    this.boss.world.addEntity(fireballs[i-1]);
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
        public boolean shouldExecute() {
            if(this.boss.isAggressive() && this.boss.getAttackTarget() != null){
                return this.boss.getHealth() <= this.boss.getMaxHealth() / 2;
            }
            return false;
        }

        @Override
        public void startExecuting() {
            this.counter = 0;
        }

        @Override
        public void tick(){
            if(this.counter >= 350){
                int typeNum = this.boss.rand.nextInt(10);
                int counterIn = 0;
                boolean maxAttempt = false;
                LivingEntity target = this.boss.getAttackTarget();
                BlockPos blockPos;
                double posX;
                double posZ;
                double posY;
                Random random = this.boss.rand;
                if(typeNum <= 3){ // <= 3
                    HardZPigMan[] pigMen = new HardZPigMan[]{
                            new HardZPigMan(ModEntities.HARD_Z_PIGMAN, this.boss.world),
                            new HardZPigMan(ModEntities.HARD_Z_PIGMAN, this.boss.world),
                            new HardZPigMan(ModEntities.HARD_Z_PIGMAN, this.boss.world)};
                    for (int i = 0; i < 3; i++){
                        do {
                            posX = this.boss.getPosX() + random.nextDouble() * 2;
                            posZ = this.boss.getPosZ() + random.nextDouble() * 2;
                            posY = this.boss.getPosY();
                            blockPos = new BlockPos(posX, posY, posZ);
                            pigMen[i].setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            if(counterIn >= 30){
                                maxAttempt = true;
                            }
                            counterIn++;
                        }while (isNotSpawnable(pigMen[i]) && !maxAttempt);
                        if(!maxAttempt){
                            pigMen[i].onInitialSpawn(this.boss.world, this.boss.world.getDifficultyForLocation(blockPos), SpawnReason.EVENT, null, null );
                            pigMen[i].setAttackTarget(target);
                            this.boss.world.playSound(null, this.boss.getPosition(), SoundEvents.ENTITY_ZOMBIE_PIGMAN_ANGRY, SoundCategory.HOSTILE, 1.0F, 1.0F);
                            this.boss.world.addEntity(pigMen[i]);
                        }
                    }

                }else if(typeNum <= 6){ // <= 6
                    HardWitherSkeleton[] witherSkeletons = new HardWitherSkeleton[]{
                            new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, this.boss.world),
                            new HardWitherSkeleton(ModEntities.HARD_WITHER_SKELETON, this.boss.world)
                    };
                    for (int i = 0; i < 2; i++) {
                        do {
                            posX = this.boss.getPosX() + random.nextDouble() * 4;
                            posZ = this.boss.getPosZ() + random.nextDouble() * 4;
                            posY = this.boss.getPosY();
                            blockPos = new BlockPos(posX, posY, posZ);
                            witherSkeletons[i].setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            if (counterIn >= 30) {
                                maxAttempt = true;
                            }
                            counterIn++;
                        } while (isNotSpawnable(witherSkeletons[i]) && !maxAttempt);
                        if(!maxAttempt){
                            witherSkeletons[i].onInitialSpawn(this.boss.world, this.boss.world.getDifficultyForLocation(blockPos), SpawnReason.EVENT, null, null );
                            witherSkeletons[i].setAttackTarget(target);
                            this.boss.world.playSound(null, this.boss.getPosition(), SoundEvents.ENTITY_ZOMBIE_PIGMAN_ANGRY, SoundCategory.HOSTILE, 1.0F, 1.0F);
                            this.boss.world.addEntity(witherSkeletons[i]);
                        }
                    }

                }else {
                    HardBlaze blaze = new HardBlaze(ModEntities.HARD_BLAZE, this.boss.world);
                    do {
                        posX = this.boss.getPosX() + random.nextDouble() * 4;
                        posZ = this.boss.getPosZ() + random.nextDouble() * 4;
                        posY = this.boss.getPosY();
                        blockPos = new BlockPos(posX, posY, posZ);
                        blaze.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        if (counterIn >= 30) {
                            maxAttempt = true;
                        }
                        counterIn++;
                    } while (isNotSpawnable(blaze) && !maxAttempt);
                    if(!maxAttempt){
                        blaze.onInitialSpawn(this.boss.world, this.boss.world.getDifficultyForLocation(blockPos), SpawnReason.EVENT, null, null );
                        blaze.setAttackTarget(target);
                        this.boss.world.playSound(null, this.boss.getPosition(), SoundEvents.ENTITY_ZOMBIE_PIGMAN_ANGRY, SoundCategory.HOSTILE, 1.0F, 1.0F);
                        this.boss.world.addEntity(blaze);
                    }
                }
                this.counter = 0;
            }
            this.counter++;
        }

        private boolean isNotSpawnable(MonsterEntity monsterEntity){
            return !this.boss.world.checkNoEntityCollision(monsterEntity) && !this.boss.world.hasNoCollisions(monsterEntity) && this.boss.world.containsAnyLiquid(monsterEntity.getBoundingBox());
        }
    }
}
