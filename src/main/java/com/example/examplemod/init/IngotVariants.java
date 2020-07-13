package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.weapons.IngotVariantSwords;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.ToolType;

import java.util.Locale;

public enum IngotVariants {
    MUTABLE_ALLOY(3,-2.4F, IngotVariantTiers.WEAK_VARIANT),
    WEAK_BLAZING_AllOY(4,-2.4F, IngotVariantTiers.WEAK_VARIANT),
    WEAK_FREEZING_ALLOY(4,-2.4F, IngotVariantTiers.WEAK_VARIANT),
    WEAK_ENDER_ALLOY(4,-2.4F, IngotVariantTiers.WEAK_ENDER),
    BLAZING_ALLOY(4,-2.4F, IngotVariantTiers.VARIANT),
    FREEZING_ALLOY(4,-2.4F, IngotVariantTiers.VARIANT),
    ENDER_ALLOY(4,-2.4F, IngotVariantTiers.ENDER);

    // Only Mutable_ingot will have an ore so it is made elsewhere

    //Item Tiers init in another class


    private final int atkDmg;
    private final float atkSpeed;
    private final IItemTier tier;


    private final LazyValue<Item> ingotItems;
    private final LazyValue<Block> ingotBlocks;
    private final LazyValue<SwordItem> ingotSwordItems;
//    private final LazyValue<AxeItem> ingotAxeItems;
//    private final LazyValue<PickaxeItem> ingotPickaxeItems;
//    private final LazyValue<ShovelItem> ingotShovelItems;
//    private final LazyValue<HoeItem> ingotHoeItems;
//    private final LazyValue<ArmorItem> ingotHelmetItems;
//    private final LazyValue<ArmorItem> ingotChestplateItems;
//    private final LazyValue<ArmorItem> ingotLeggingsItems;
//    private final LazyValue<ArmorItem> ingotBootsItems;

    IngotVariants(int attackDmg, float attackspeed, IItemTier tier){

        this.atkDmg = attackDmg;
        this.atkSpeed = attackspeed;
        this.tier = tier;

        ingotItems = new LazyValue<>(() -> new Item(new Item.Properties().group(ExampleMod.ITEM_GROUP)));
        ingotBlocks = new LazyValue<>(() -> new Block(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5f,6f)
                .sound(SoundType.METAL)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
        ));
        ingotSwordItems = new LazyValue<>(()-> new IngotVariantSwords(this.tier, this.atkDmg, this.atkSpeed, this.getVariant(), new Item.Properties()));
    }


    public IngotVariants getVariant(){
        return this;
    }

    public String getIngotVariantName(){
        return name().toLowerCase(Locale.ROOT);
    }

    public Item getIngotItem() {
        return ingotItems.getValue();
    }

    public Block getIngotBlock(){
        return ingotBlocks.getValue();
    }

    public SwordItem getIngotSwordItem(){
        return ingotSwordItems.getValue();
    }

}
