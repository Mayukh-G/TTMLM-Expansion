package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.armor.VariantArmor;
import com.example.examplemod.item.tools.IngotVariantAxes;
import com.example.examplemod.item.tools.IngotVariantPickaxes;
import com.example.examplemod.item.tools.IngotVariantShovels;
import com.example.examplemod.item.weapons.IngotVariantSwords;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.ToolType;

import java.util.Locale;

public enum IngotVariants {
    MUTABLE_ALLOY(2,-2.4F, IngotVariantTiers.MUTABLE, VariantArmorMaterial.MUTABLE),
    WEAK_BLAZING_AllOY(4,-2.4F, IngotVariantTiers.WEAK_VARIANT_BLAZING, VariantArmorMaterial.WEAK_VARIANT_BLAZING),
    WEAK_FREEZING_ALLOY(4,-2.4F, IngotVariantTiers.WEAK_VARIANT_FREEZING, VariantArmorMaterial.WEAK_VARIANT_FREEZING),
    WEAK_ENDER_ALLOY(4,-2.8F, IngotVariantTiers.WEAK_ENDER, VariantArmorMaterial.WEAK_ENDER),
    BLAZING_ALLOY(4,-2.4F, IngotVariantTiers.VARIANT_BLAZING, VariantArmorMaterial.VARIANT_BLAZING),
    FREEZING_ALLOY(4,-2.4F, IngotVariantTiers.VARIANT_FREEZING, VariantArmorMaterial.VARIANT_FREEZING),
    ENDER_ALLOY(4,-2.8F, IngotVariantTiers.ENDER, VariantArmorMaterial.ENDER);

    // Only Mutable_ingot will have an ore so it is made elsewhere

    //Item Tiers init in another class


    private final int atkDmg;
    private final float atkSpeed;
    private final IItemTier tier;
    private final IArmorMaterial material;


    private final LazyValue<Item> ingotItems;
    private final LazyValue<Block> ingotBlocks;
    private final LazyValue<SwordItem> ingotSwordItems;
    private final LazyValue<AxeItem> ingotAxeItems;
    private final LazyValue<PickaxeItem> ingotPickaxeItems;
    private final LazyValue<ShovelItem> ingotShovelItems;
//    private final LazyValue<HoeItem> ingotHoeItems;
    private final LazyValue<ArmorItem> ingotHelmetItems;
    private final LazyValue<ArmorItem> ingotChestplateItems;
    private final LazyValue<ArmorItem> ingotLeggingsItems;
    private final LazyValue<ArmorItem> ingotBootsItems;

    IngotVariants(int attackDmg, float attackspeed, IItemTier tier, IArmorMaterial material){

        this.atkDmg = attackDmg;
        this.atkSpeed = attackspeed;
        this.tier = tier;
        this.material = material;

        ingotItems = new LazyValue<>(() -> new Item(new Item.Properties().group(ExampleMod.ITEM_GROUP)));
        ingotBlocks = new LazyValue<>(() -> new Block(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5f,6f)
                .sound(SoundType.METAL)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
        ));
        ingotSwordItems = new LazyValue<>(()-> new IngotVariantSwords(this.tier, this.atkDmg, this.atkSpeed, this.getVariant(), new Item.Properties()));
        ingotHelmetItems = new LazyValue<>(() -> new VariantArmor(this.material, EquipmentSlotType.HEAD, new Item.Properties()));
        ingotChestplateItems = new LazyValue<>(() -> new VariantArmor(this.material, EquipmentSlotType.CHEST, new Item.Properties()));
        ingotLeggingsItems = new LazyValue<>(() -> new VariantArmor(this.material, EquipmentSlotType.LEGS, new Item.Properties()));
        ingotBootsItems = new LazyValue<>(() -> new VariantArmor(this.material, EquipmentSlotType.FEET, new Item.Properties()));
        ingotPickaxeItems = new LazyValue<>(() -> new IngotVariantPickaxes(this.tier, this.atkDmg - 3, (float)(this.atkSpeed - 0.3), this.getVariant(), new Item.Properties()));
        ingotAxeItems = new LazyValue<>(() -> new IngotVariantAxes(this.tier, this.atkDmg + 1, (float)(this.atkSpeed - 0.5), this.getVariant(), new Item.Properties()));
        ingotShovelItems = new LazyValue<>(() -> new IngotVariantShovels(this.tier, this.atkDmg - 4, (float)(this.atkSpeed - 0.3), this.getVariant(), new Item.Properties()));
    }


    public IngotVariants getVariant(){
        return this;
    }

    public String getVariantName(){
        return name().toLowerCase(Locale.ROOT);
    }

    public Item getIngotItem() {
        return ingotItems.getValue();
    }

    public Block getIngotBlock(){
        return ingotBlocks.getValue();
    }

    public SwordItem getSwordItem(){
        return ingotSwordItems.getValue();
    }

    public ArmorItem getHelmetItem() {
        return ingotHelmetItems.getValue();
    }

    public ArmorItem getChestplateItem(){
        return ingotChestplateItems.getValue();
    }

    public ArmorItem getLegginsItem(){
        return ingotLeggingsItems.getValue();
    }

    public ArmorItem getBootsItem(){
        return ingotBootsItems.getValue();
    }

    public PickaxeItem getPickaxeItem(){
        return ingotPickaxeItems.getValue();
    }

    public AxeItem getAxeItem() {
        return ingotAxeItems.getValue();
    }

    public ShovelItem getShovelItem() { return ingotShovelItems.getValue(); }

}
