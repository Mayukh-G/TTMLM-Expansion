package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum VariantArmorMaterial implements IArmorMaterial {
    MUTABLE(TTMLM.MOD_ID + ":mutable", 15, new int[]{2,5,6,2},10, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.of(IngotVariants.MUTABLE_ALLOY.getIngotItem());
    }),
    WEAK_VARIANT_BLAZING(TTMLM.MOD_ID + ":weak_variant_blazing", 45, new int[]{3,5,7,3},12, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.of(IngotVariants.WEAK_BLAZING_AllOY.getIngotItem());
    }),
    WEAK_VARIANT_FREEZING(TTMLM.MOD_ID + ":weak_variant_freezing", 45, new int[]{3,5,7,3},12, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, () -> {
        return Ingredient.of(IngotVariants.WEAK_FREEZING_ALLOY.getIngotItem());
    }),
    WEAK_ENDER(TTMLM.MOD_ID + ":weak_ender", 50, new int[]{5,8,9,5},12, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, () -> {
        return Ingredient.of(IngotVariants.WEAK_ENDER_ALLOY.getIngotItem());
    }),
    VARIANT_BLAZING(TTMLM.MOD_ID + ":variant_blazing", 70, new int[]{6,8,10,6},13, SoundEvents.ARMOR_EQUIP_IRON, 4.0F, () -> {
        return Ingredient.of(IngotVariants.BLAZING_ALLOY.getIngotItem());
    }),
    VARIANT_FREEZING(TTMLM.MOD_ID + ":variant_freezing", 70, new int[]{6,8,10,6},13, SoundEvents.ARMOR_EQUIP_IRON, 4.0F, () -> {
        return Ingredient.of(IngotVariants.FREEZING_ALLOY.getIngotItem());
    }),
    ENDER(TTMLM.MOD_ID + ":ender", 100, new int[]{7,9,11,7},13, SoundEvents.ARMOR_EQUIP_DIAMOND, 5.0F, () -> {
        return Ingredient.of(IngotVariants.ENDER_ALLOY.getIngotItem());
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

    @Override
    public int getDurabilityForSlot(EquipmentSlotType equipmentSlotType) {
        return MAX_DAMAGE_ARRAY[equipmentSlotType.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType equipmentSlotType) {
        return this.damageReductionAmountArray[equipmentSlotType.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @NotNull
    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
