package com.example.ttmlm.item.weapons;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.init.IngotVariants;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;


public class IngotVariantSwords extends SwordItem {

    private final IngotVariants variant;
    private static boolean loopCheck = false;

    public IngotVariantSwords(IItemTier tier, int attackDamage, float attackSpeed, IngotVariants variants, Item.Properties builder) {
        super(tier, attackDamage, attackSpeed, builder.tab(TTMLM.ITEM_GROUP_COMBAT));
        this.variant = variants;
    }

    @Override //Adding lore text to explain item function
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        switch (this.variant) {
            case WEAK_BLAZING_AllOY:
                p_77624_3_.add(new StringTextComponent("Fire Slash :").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC)
                                .append("3 seconds").withStyle(TextFormatting.LIGHT_PURPLE));
                break;
            case WEAK_FREEZING_ALLOY:
                p_77624_3_.add(new StringTextComponent("Slowness I :").withStyle(TextFormatting.AQUA, TextFormatting.ITALIC)
                        .append("3 seconds").withStyle(TextFormatting.LIGHT_PURPLE));
                break;
            case WEAK_ENDER_ALLOY:
                p_77624_3_.add(new StringTextComponent("Marked For Slaughter I :").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC)
                        .append(" 20% Hp execute").withStyle(TextFormatting.LIGHT_PURPLE));
                break;
            case BLAZING_ALLOY:
                p_77624_3_.add(new StringTextComponent("Fire Slash :").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC)
                        .append("10 seconds").withStyle(TextFormatting.LIGHT_PURPLE));
                break;
            case FREEZING_ALLOY:
                p_77624_3_.add(new StringTextComponent("Slowness III :").withStyle(TextFormatting.AQUA, TextFormatting.ITALIC)
                        .append("3 seconds").withStyle(TextFormatting.LIGHT_PURPLE));
                break;
            case ENDER_ALLOY:
                p_77624_3_.add(new StringTextComponent("Marked For Slaughter II :").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC)
                        .append(" 30% Hp execute").withStyle(TextFormatting.LIGHT_PURPLE));
        }
    }

    public void onAttack(LivingEntity entityAttacked, @Nullable PlayerEntity playerEntity, float amountDealt) {
        //Line underneath mitigated dmg
        amountDealt = CombatRules.getDamageAfterAbsorb(amountDealt, (float) entityAttacked.getArmorValue(), (float) entityAttacked.getAttribute(Attributes.ARMOR_TOUGHNESS).getValue());
        World attackedEntityWorld = entityAttacked.level;
        switch (this.variant) {
            case WEAK_BLAZING_AllOY:
                if (!entityAttacked.isOnFire() && !entityAttacked.fireImmune()) { //Play sound only if Entity is not on fire
                    attackedEntityWorld.playSound(null, entityAttacked.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
                entityAttacked.setSecondsOnFire(3);
                break;

            case WEAK_FREEZING_ALLOY: //Slow entity
                entityAttacked.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 0, false, true));
                break;

            case WEAK_ENDER_ALLOY: //Execute by either player or random source and play ender dragon growl (this causes a loop the event is called again which resets the loop causing a stackoverflow)
                if (entityAttacked.getHealth() - amountDealt <= entityAttacked.getMaxHealth() * 0.2 && !attackedEntityWorld.isClientSide) {
                    if (playerEntity != null) {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.hurt(DamageSource.playerAttack(playerEntity), entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, playerEntity.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    } else {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.hurt(DamageSource.MAGIC, entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, entityAttacked.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    }
                }
                break;

            case BLAZING_ALLOY: //Burn for longer
                if (!entityAttacked.isOnFire() && !entityAttacked.fireImmune()) {
                    attackedEntityWorld.playSound(null, entityAttacked.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
                entityAttacked.setSecondsOnFire(10);
                break;

            case FREEZING_ALLOY: //Stronger Slow
                entityAttacked.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60, 2, false, true));
                break;

            case ENDER_ALLOY: //Execute but higher threshold
                if (entityAttacked.getHealth() - amountDealt <= entityAttacked.getMaxHealth() * 0.3 && !attackedEntityWorld.isClientSide) {
                    if (playerEntity != null) {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.hurt(DamageSource.playerAttack(playerEntity), entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, playerEntity.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    } else {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.hurt(DamageSource.MAGIC, entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, entityAttacked.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    }
                }
                break;
        }
    }
}
