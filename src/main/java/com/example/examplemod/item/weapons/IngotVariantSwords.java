package com.example.examplemod.item.weapons;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;


public class IngotVariantSwords extends SwordItem { //FINISH EFFECT FOR EACH TYPE

    private final IngotVariants variant;
//    private final EffectInstance weakFreezingEffect = new EffectInstance(Effects.SLOWNESS, 30,0,false,true);
//    private final EffectInstance freezingEffect = new EffectInstance(Effects.SLOWNESS, 30,2,false,true);

    public IngotVariantSwords(IItemTier tier, int attackDamage, float attackSpeed, IngotVariants variants, Item.Properties builder){
        super(tier, attackDamage, attackSpeed, builder.group(ExampleMod.ITEM_GROUP));
        this.variant = variants;
    }


    public void onAttack(LivingEntity entityAttacked){
        switch (this.variant){
            case WEAK_BLAZING_AllOY: entityAttacked.setFire(3);
            break;
            case WEAK_FREEZING_ALLOY: entityAttacked.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 30,0,false,true));
            break;
            case BLAZING_ALLOY: entityAttacked.setFire(10);
            break;
            case FREEZING_ALLOY:
                entityAttacked.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 30,2,false,true));
            break;
            default:
        }
    }
}
