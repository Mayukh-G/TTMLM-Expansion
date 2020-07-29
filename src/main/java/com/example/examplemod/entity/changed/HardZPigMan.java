package com.example.examplemod.entity.changed;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariants;
import com.example.examplemod.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class HardZPigMan extends ZombiePigmanEntity implements IAbstractHardZombie {
    public static final String name = "hard_zombie_pigman";

    public HardZPigMan(EntityType<?> type, World world) {
        super(ModEntities.HARD_Z_PIGMAN, world);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(IngotVariants.WEAK_BLAZING_AllOY.getIngotSwordItem()));
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(IngotVariants.WEAK_BLAZING_AllOY.getHelmetItem()));
        this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0F;
        this.inventoryArmorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0;
    }




    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(5.0D);
    }

    @NotNull
    @Override
    protected ResourceLocation getLootTable() {
        if(this.getCustomName() == this.leaderName) {
            return ExampleMod.getID("entities/leader_zombie_type");
        }else {
            return super.getLootTable();
        }
    }

    @Override
    public void setLeaderAttributes() {
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(this.getAttribute(SharedMonsterAttributes.ARMOR).getValue() * 4.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue() + 0.05D);
    }

}
