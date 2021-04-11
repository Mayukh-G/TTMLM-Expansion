package com.example.ttmlm.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum IngotVariantTiers implements IItemTier {
    MUTABLE(2, 250, 5.5F, 2F, 15, () -> {
        return Ingredient.of(IngotVariants.MUTABLE_ALLOY.getIngotItem());
    }),
    WEAK_VARIANT_BLAZING(2, 550, 6.5F, 2F, 10, () -> {
        return Ingredient.of(IngotVariants.WEAK_BLAZING_AllOY.getIngotItem());
    }),
    WEAK_VARIANT_FREEZING(2, 550, 6.5F, 2F, 10, () -> {
        return Ingredient.of(IngotVariants.WEAK_FREEZING_ALLOY.getIngotItem());
    }),
    WEAK_ENDER(3, 2000, 11.0F, 3F, 10, () -> {
        return Ingredient.of(IngotVariants.WEAK_ENDER_ALLOY.getIngotItem());
    }),
    VARIANT_BLAZING(3, 2500, 10.0F, 4F, 10, () -> {
        return Ingredient.of(IngotVariants.BLAZING_ALLOY.getIngotItem());
    }),
    VARIANT_FREEZING(3, 2500, 10.0F, 4F, 10, () -> {
        return Ingredient.of(IngotVariants.FREEZING_ALLOY.getIngotItem());
    }),
    ENDER(4, 3000, 12.0F, 5F, 19, () -> {
        return Ingredient.of(IngotVariants.ENDER_ALLOY.getIngotItem());
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

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.get();
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @NotNull
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
