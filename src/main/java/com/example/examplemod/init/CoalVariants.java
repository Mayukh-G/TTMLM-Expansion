package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.LazyValue;

import java.util.Locale;

public enum CoalVariants {
    BLAZING_CARBON,
    FREEZING_CARBON;

    private final LazyValue<Block> storageBlock;
    private final LazyValue<Item> coalVariant;

    CoalVariants() {
        storageBlock = new LazyValue<>(() -> new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(3f,10f)
                .sound(SoundType.STONE)
                .lightValue(6)
        ));
        coalVariant = new LazyValue<>(() -> new Item(new Item.Properties()
                .group(ExampleMod.ITEM_GROUP)
        ));
    }
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public Block getStorageBlock() {
        return storageBlock.getValue();
    }

    public Item getCoalVariant(){
        return coalVariant.getValue();
    }

}
