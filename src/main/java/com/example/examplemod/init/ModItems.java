package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    static final Map<String, BlockItem> BLOCKS_TO_REGISTER = new LinkedHashMap<>();
    public static void registerALL(RegistryEvent.Register<Item> event){

    }
//    private static <T extends Item> T register(String name, T item) {
//        ResourceLocation id = new ResourceLocation()
//    }
}
