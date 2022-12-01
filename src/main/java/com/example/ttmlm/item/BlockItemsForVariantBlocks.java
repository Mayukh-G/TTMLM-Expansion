package com.example.ttmlm.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockItemsForVariantBlocks extends BlockItem {
    private final String name;

    public BlockItemsForVariantBlocks(Block block, Properties builder, String blockName){
        super(block, builder);
        this.name = blockName;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        if (this.name.equals("blazing_carbon_block")){
            return 25000;
        }
        return 0;
    }
}
