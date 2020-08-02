package com.example.examplemod.entity.changed;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class HardZombie extends ZombieEntity implements IAbstractHardZombie {
    public static final String name = "hard_zombie";
    public boolean isLEADER;

    public HardZombie(EntityType<?> type, World world){
        super( ModEntities.HARD_ZOMBIE, world);
        this.isLEADER = rand.nextInt(99) <= 5;
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
    public void tick() {
        super.tick();
        TextComponent currentName = (TextComponent) this.getCustomName();
        if (currentName != this.leaderName && this.isLEADER){
            this.setCustomName(this.leaderName);
            this.setCustomNameVisible(true);
        }
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
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(this.getAttribute(SharedMonsterAttributes.ARMOR).getValue() * 4.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() + 0.05D);
    }
}
