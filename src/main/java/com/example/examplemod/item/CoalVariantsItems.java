package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CoalVariantsItems extends Item {
    private final String name;

    public CoalVariantsItems(String variantName, Properties builder){
        super(builder.group(ExampleMod.ITEM_GROUP));
        this.name = variantName;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        if (this.name.equals("blazing_carbon")){
            return 3200;
        }
        else if(this.name.equals("freezing_carbon")){
            return 0;
        }
        return 0;
    }
}
