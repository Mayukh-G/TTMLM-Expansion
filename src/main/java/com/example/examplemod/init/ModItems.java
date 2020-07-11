package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    static final Map<String, BlockItem> BLOCKS_TO_REGISTER = new LinkedHashMap<>();
    public static void registerALL(RegistryEvent.Register<Item> event){
        if (!event.getName().equals(ForgeRegistries.ITEMS.getRegistryName())) return;

        //Blocks
        BLOCKS_TO_REGISTER.forEach(ModItems::register);

        //Items
        //Coal Variant Items
        for(CoalVariants coalVariants : CoalVariants.values()){
            register(coalVariants.getCoalVariantName(), coalVariants.getCoalVariantItem());
        }
        //Ingot Variant Items
        for(IngotVariants ingotVariants: IngotVariants.values()){
            register(ingotVariants.getIngotVariantName(), ingotVariants.getIngotItem());
        }
        //Ingot Variant Sword Items
        for(IngotVariants ingotVariants: IngotVariants.values()){
            register(ingotVariants.getIngotVariantName()+ "_sword", ingotVariants.getIngotSwordItem());
        }


    }
    private static <T extends Item> T register(String name, T item) {
        ResourceLocation id = ExampleMod.getID(name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
