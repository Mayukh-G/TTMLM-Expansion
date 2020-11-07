package com.example.ttmlm.init;

import com.example.ttmlm.block.OreBlocks;
import com.example.ttmlm.item.CoalVariantsItems;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;
import net.minecraftforge.common.ToolType;

import java.util.Locale;

public enum CoalVariants {
    BLAZING_CARBON,
    FREEZING_CARBON;

    private final LazyValue<OreBlocks> oreBlocks;
    private final LazyValue<Block> coalVariantBlock;
    private final LazyValue<SlabBlock> coalVariantBlocksUpper;
    private final LazyValue<SlabBlock> coalVariantBlocksLower;
    private final LazyValue<Item> coalVariantItem;


    CoalVariants() {
        oreBlocks = new LazyValue<>(OreBlocks::new);
        coalVariantBlock = new LazyValue<>(() -> new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3f,10f)
                .sound(SoundType.STONE)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
        ));
        coalVariantBlocksUpper =  new LazyValue<>(() -> new SlabBlock(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3f,10f)
                .sound(SoundType.STONE)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
        ));
        coalVariantBlocksLower =  new LazyValue<>(() -> new SlabBlock(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3f,10f)
                .sound(SoundType.STONE)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
        ));
        coalVariantItem = new LazyValue<>(() -> new CoalVariantsItems(this.getCoalVariantName(), new Item.Properties()));
    }

    public String getCoalVariantName() { return name().toLowerCase(Locale.ROOT); }

    public OreBlocks getOreBlocks(){ return oreBlocks.getValue(); }

    public Block getCoalVariantBlock() { return coalVariantBlock.getValue(); }

    public Block getCoalVariantBlockUpper(){
        return coalVariantBlocksUpper.getValue();
    }

    public Block getCoalVariantBlockLower(){
        return coalVariantBlocksLower.getValue();
    }

    public Item getCoalVariantItem(){ return coalVariantItem.getValue(); }

}
