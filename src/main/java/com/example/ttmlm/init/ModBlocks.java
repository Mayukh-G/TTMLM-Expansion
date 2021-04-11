package com.example.ttmlm.init;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.block.OreBlocks;
import com.example.ttmlm.item.BlockItemsForVariantBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ModBlocks {
    public static Block frozenGeode;
    public static Block mutableAlloyOre;
    public static Block mutableAlloyOreNether;
    public static Block mutableAlloyOreEnd;


    public static void  registerAll(RegistryEvent.Register<Block> event) {
        if (!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;

        frozenGeode = register("frozen_geode", new Block(Block.Properties.of(Material.ICE)
                .strength(0.98F,0.5F)
                .sound(SoundType.GLASS)
        ));

        //IngotVariant Registration
        mutableAlloyOre = register("mutable_alloy_ore", new OreBlocks());
        mutableAlloyOreNether = register("nether_mutable_alloy_ore", new OreBlocks());
        mutableAlloyOreEnd = register("end_mutable_alloy_ore", new OreBlocks());

        for (IngotVariants ingotVariants: IngotVariants.values()){
            register(ingotVariants.getVariantName() + "_block", ingotVariants.getIngotBlock());
        }


        // CoalVariant registration
        for(CoalVariants coalVariants : CoalVariants.values()){
            //Custom BlockItem Subclass so item can be detected as fuel
            register(coalVariants.getCoalVariantName() + "_block", coalVariants.getCoalVariantBlock(),
                    new BlockItemsForVariantBlocks(coalVariants.getCoalVariantBlock(), new Item.Properties().tab(TTMLM.ITEM_GROUP_BLOCK).tab(TTMLM.ITEM_GROUP_RESOURCES), coalVariants.getCoalVariantName() + "_block")
            );
        }

        for(CoalVariants coalVariants: CoalVariants.values()){
            register(coalVariants.getCoalVariantName() + "_block_upper", coalVariants.getCoalVariantBlockUpper());
        }

        for(CoalVariants coalVariants: CoalVariants.values()){
            register(coalVariants.getCoalVariantName() + "_block_lower", coalVariants.getCoalVariantBlockLower());
        }

        for (CoalVariants coalVariants : CoalVariants.values()){

            register(coalVariants.getCoalVariantName() + "_ore", coalVariants.getOreBlocks());
        }

    }

    private static <T extends Block> T register(String name,T block ) {
        BlockItem item = new BlockItem(block, new Item.Properties().tab(TTMLM.ITEM_GROUP_BLOCK));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, @Nullable BlockItem item) {
        ResourceLocation id = TTMLM.getID(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);
        if (item != null){
            ModItems.BLOCKS_TO_REGISTER.put(name, item);
        }
        return block;
    }

}
