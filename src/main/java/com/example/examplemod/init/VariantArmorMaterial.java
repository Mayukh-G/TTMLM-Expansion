package com.example.examplemod.init;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum VariantArmorMaterial implements IArmorMaterial {
    MUTABLE("mutable", 15, new int[]{2,5,6,2},10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.fromItems(IngotVariants.MUTABLE_ALLOY.getIngotItem());
    }),
    WEAK_VARIANT_BLAZING("weak_variant_blazing", 25, new int[]{3,5,7,3},12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_BLAZING_AllOY.getIngotItem());
    }),
    WEAK_VARIANT_FREEZING("weak_variant_freezing", 25, new int[]{3,5,7,3},12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_FREEZING_ALLOY.getIngotItem());
    }),
    WEAK_ENDER("weak_ender", 38, new int[]{4,7,8,4},12, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, () -> {
        return Ingredient.fromItems(IngotVariants.WEAK_ENDER_ALLOY.getIngotItem());
    }),
    VARIANT_BLAZING("variant_blazing", 40, new int[]{5,7,9,5},13, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F, () -> {
        return Ingredient.fromItems(IngotVariants.BLAZING_ALLOY.getIngotItem());
    }),
    VARIANT_FREEZING("variant_freezing", 40, new int[]{5,7,9,5},13, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 3.0F, () -> {
        return Ingredient.fromItems(IngotVariants.FREEZING_ALLOY.getIngotItem());
    }),
    ENDER("ender", 42, new int[]{5,8,10,5},13, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.5F, () -> {
        return Ingredient.fromItems(IngotVariants.ENDER_ALLOY.getIngotItem());
    });


    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyValue<Ingredient> repairMaterial;

    VariantArmorMaterial(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, SoundEvent equipSoundIn, float toughnessIn, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = nameIn;
        this.maxDamageFactor = maxDamageFactorIn;
        this.damageReductionAmountArray = damageReductionAmountsIn;
        this.enchantability = enchantabilityIn;
        this.soundEvent = equipSoundIn;
        this.toughness = toughnessIn;
        this.repairMaterial = new LazyValue<>(repairMaterialSupplier);
    }


    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    @Nonnull
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }
}
