package com.example.ttmlm.item.armor;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.VariantArmorMaterial;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.UUID;

public class VariantArmor extends ArmorItem { //Stop being lazy and do recipies and textures and models
    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private static final UUID[] HEALTH_BONUS_PLUS_40 = new UUID[]{UUID.fromString("bc5640bf-c50a-4943-aba6-2cc496bc4dc9"), UUID.fromString("290b0fe4-da68-4e7f-8e8f-2da5149c7b73")};
    private static final UUID[] HEALTH_BONUS_PLUS_35 = new UUID[]{UUID.fromString("c42da457-9c55-4925-ab23-80b0569239f7"), UUID.fromString("645f9850-39fb-4d48-b52f-ea992d186004")};
    private static final UUID[] HEALTH_BONUS_PLUS_25 = new UUID[]{UUID.fromString("1fee06d1-744e-40e2-b77d-b16c7e783536"),UUID.fromString("034e442d-b194-49bb-a243-ccfe1547be7c")};
    private static final UUID weakSpeed = UUID.fromString("ad59ac6a-cc83-4f37-99e5-03bb036af7af");
    private static final UUID Speed = UUID.fromString("b1bd846c-eabe-404e-b8eb-f4c0a3af174d");


    public VariantArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builder){
        super(materialIn, slot, builder.group(TTMLM.ITEM_GROUP_COMBAT));
    }

    @Override
    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot);
        if (equipmentSlot == this.slot) {
            multimap.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", this.damageReduceAmount, AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));

            if (this.slot == EquipmentSlotType.FEET) { //ADD SPEED BOOST TO BOOTS
                if (this.material == VariantArmorMaterial.ENDER || this.material == VariantArmorMaterial.VARIANT_BLAZING) {
                    multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(Speed,"Movement Speed", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE));
                } else if (this.material == VariantArmorMaterial.WEAK_VARIANT_BLAZING || this.material == VariantArmorMaterial.WEAK_ENDER) {
                    multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(weakSpeed,"Movement Speed", 0.3D, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }
            //ADD HEALTH BOOST TO OTHER
            int index;
            if ((this.material == VariantArmorMaterial.WEAK_VARIANT_FREEZING || this.material == VariantArmorMaterial.WEAK_ENDER) && (this.slot == EquipmentSlotType.CHEST || this.slot == EquipmentSlotType.LEGS)) {
                if(this.slot == EquipmentSlotType.CHEST){
                    index = 0;
                }else {index = 1;}
                multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(HEALTH_BONUS_PLUS_35[index],"Heath Bonus", 0.35D, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            else if(this.material == VariantArmorMaterial.VARIANT_FREEZING || this.material == VariantArmorMaterial.ENDER){
                if(this.slot == EquipmentSlotType.CHEST || this.slot == EquipmentSlotType.LEGS){
                    if(this.slot == EquipmentSlotType.CHEST) { index = 0; }
                    else { index = 1;}
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(HEALTH_BONUS_PLUS_40[index],"Heath Bonus", 0.4D, AttributeModifier.Operation.MULTIPLY_BASE));
                }else{
                    if(this.slot == EquipmentSlotType.HEAD) { index = 0; }
                    else { index = 1; }
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(HEALTH_BONUS_PLUS_25[index],"Heath Bonus", 0.25D, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            } else if(this.material == VariantArmorMaterial.VARIANT_BLAZING){
                if(this.slot == EquipmentSlotType.CHEST || this.slot == EquipmentSlotType.LEGS){
                    if(this.slot == EquipmentSlotType.CHEST) { index = 0; }
                    else { index = 1;}
                    multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), new AttributeModifier(HEALTH_BONUS_PLUS_25[index],"Heath Bonus", 0.25D, AttributeModifier.Operation.MULTIPLY_BASE));
                }
            }
        }

        return multimap;
    }

}
