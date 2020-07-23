package com.example.examplemod.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum IngotVariantTiers implements IItemTier {
    MUTABLE(2, 250, 5.5F, 2F, 10, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_BLAZING_AllOY.getIngotItem());
    }),
    WEAK_VARIANT_BLAZING(2, 550, 6.5F, 2F, 12, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_BLAZING_AllOY.getIngotItem());
    }),
    WEAK_VARIANT_FREEZING(2, 550, 6.5F, 2F, 12, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_FREEZING_ALLOY.getIngotItem());
    }),
    WEAK_ENDER(3, 2000, 12.0F, 3F, 12, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_ENDER_ALLOY.getIngotItem());
    }),
    VARIANT_BLAZING(3, 2500, 10.0F, 4F, 13, () -> {
        return Ingredient.fromItems(IngotVariants.BLAZING_ALLOY.getIngotItem());
    }),
    VARIANT_FREEZING(3, 2500, 10.0F, 4F, 13, () -> {
        return Ingredient.fromItems(IngotVariants.FREEZING_ALLOY.getIngotItem());
    }),
    ENDER(4, 3000, 15.0F, 5F, 11, () -> {
        return Ingredient.fromItems(IngotVariants.ENDER_ALLOY.getIngotItem());
    });


    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    IngotVariantTiers(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyValue<>(repairMaterialIn);
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

}
