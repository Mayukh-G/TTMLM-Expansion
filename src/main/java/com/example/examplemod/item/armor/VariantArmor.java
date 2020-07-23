package com.example.examplemod.item.armor;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.VariantArmorMaterial;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.UUID;

public class VariantArmor extends ArmorItem {
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    public VariantArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builder){
        super(materialIn, slot, builder.group(ExampleMod.ITEM_GROUP));
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) { //Remove the attributes add code
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.slot) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount, AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));
        }

        if(this.slot == equipmentSlot) {
            if (this.slot == EquipmentSlotType.FEET) {
                if (this.material == VariantArmorMaterial.ENDER || this.material == VariantArmorMaterial.VARIANT_BLAZING) {
                    multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier("Movement Speed", 0.3D, AttributeModifier.Operation.ADDITION));
                } else if (this.material == VariantArmorMaterial.WEAK_VARIANT_BLAZING || this.material == VariantArmorMaterial.WEAK_ENDER) {
                    multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier("Movement Speed", 0.2D, AttributeModifier.Operation.ADDITION));
                }
            }
            if (this.material == VariantArmorMaterial.WEAK_VARIANT_FREEZING) {
                multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier("Knockback Resistance", 0.1D, AttributeModifier.Operation.ADDITION));
            }
            else if(this.material == VariantArmorMaterial.VARIANT_FREEZING){
                multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier("Knockback Resistance", 0.15D, AttributeModifier.Operation.ADDITION));
            }
        }

        return multimap;
    }
}
