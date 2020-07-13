package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.OreBlocks;
import com.example.examplemod.item.BlockItemsForVariantBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class ModBlocks {
    public static Block bigSlip;
    public static Block mutableAlloyOre;
    public static Block mutableAlloyOreNether;
    public static Block mutableAlloyOreEnd;


    public static void  registerAll(RegistryEvent.Register<Block> event) {
        if (!event.getName().equals(ForgeRegistries.BLOCKS.getRegistryName())) return;

        bigSlip = register("big_slip", new Block(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5f,6f)
                .sound(SoundType.STONE)
        ));

        //IngotVariant Registration
        mutableAlloyOre = register("mutable_alloy_ore", new OreBlocks());
        mutableAlloyOreNether = register("nether_mutable_alloy_ore", new OreBlocks());
        mutableAlloyOreEnd = register("end_mutable_alloy_ore", new OreBlocks());

        for (IngotVariants ingotVariants: IngotVariants.values()){
            register(ingotVariants.getIngotVariantName() + "_block", ingotVariants.getIngotBlock());
        }


        // CoalVariant registration
        for(CoalVariants coalVariants : CoalVariants.values()){
            //Custom BlockItem Subclass so item can be detected as fuel
            register(coalVariants.getCoalVariantName() + "_block", coalVariants.getCoalVariantBlock(),
                    new BlockItemsForVariantBlocks(coalVariants.getCoalVariantBlock(), new Item.Properties().group(ExampleMod.ITEM_GROUP), coalVariants.getCoalVariantName() + "_block")
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
        //If statement here for carbon
        BlockItem item = new BlockItem(block, new Item.Properties().group(ExampleMod.ITEM_GROUP));
        return register(name, block, item);
    }

    private static <T extends Block> T register(String name, T block, @Nullable BlockItem item) {
        ResourceLocation id = ExampleMod.getID(name);
        block.setRegistryName(id);
        ForgeRegistries.BLOCKS.register(block);
        if (item != null){
            ModItems.BLOCKS_TO_REGISTER.put(name, item);
        }
        return block;
    }

}
