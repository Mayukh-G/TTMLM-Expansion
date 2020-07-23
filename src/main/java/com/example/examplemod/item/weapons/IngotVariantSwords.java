package com.example.examplemod.item.weapons;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.init.IngotVariants;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
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
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class IngotVariantSwords extends SwordItem {

    private final IngotVariants variant;
    private static boolean loopCheck = false;

    public IngotVariantSwords(IItemTier tier, int attackDamage, float attackSpeed, IngotVariants variants, Item.Properties builder) {
        super(tier, attackDamage, attackSpeed, builder.group(ExampleMod.ITEM_GROUP));
        this.variant = variants;
    }

    @Override //Adding lore text to explain item function
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        switch (this.variant) {
            case WEAK_BLAZING_AllOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A76\u00A7oFire Slash :\u00A7d 3 seconds";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
                break;
            case WEAK_FREEZING_ALLOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A7b\u00A7oSlowness I :\u00A7d 3 seconds";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
                break;
            case WEAK_ENDER_ALLOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A74\u00A7oMarked For Slaughter I :\u00A7d 20% Hp execute";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
                break;
            case BLAZING_ALLOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A76\u00A7oFire Slash :\u00A7d 10 seconds";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
                break;
            case FREEZING_ALLOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A7b\u00A7oSlowness III :\u00A7d 3 seconds";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
                break;
            case ENDER_ALLOY:
                tooltip.add(new TextComponent() {
                    @Override
                    @Nonnull
                    public String getUnformattedComponentText() {
                        return "\u00A74\u00A7oMarked For Slaughter II :\u00A7d 30% Hp execute";
                    }

                    @Override
                    @Nonnull
                    public ITextComponent shallowCopy() {
                        return this;
                    }
                });
        }
    }

    public void onAttack(LivingEntity entityAttacked, @Nullable PlayerEntity playerEntity, float amountDealt) {
        //Line underneath mitigated dmg
        amountDealt = CombatRules.getDamageAfterAbsorb(amountDealt, (float) entityAttacked.getTotalArmorValue(), (float) entityAttacked.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue());
        World attackedEntityWorld = entityAttacked.getEntityWorld();
        switch (this.variant) {
            case WEAK_BLAZING_AllOY:
                if (!entityAttacked.isBurning() && !entityAttacked.isImmuneToFire()) { //Play sound only if Entity is not on fire
                    attackedEntityWorld.playSound(null, entityAttacked.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
                entityAttacked.setFire(3);
                break;

            case WEAK_FREEZING_ALLOY: //Slow entity
                entityAttacked.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 0, false, true));
                break;

            case WEAK_ENDER_ALLOY: //Execute by either player or random source and play ender dragon growl (this causes a loop the event is called again which resets the loop causing a stackoverflow)
                if (entityAttacked.getHealth() - amountDealt <= entityAttacked.getMaxHealth() * 0.2 && !attackedEntityWorld.isRemote()) {
                    if (playerEntity != null) {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.attackEntityFrom(DamageSource.causePlayerDamage(playerEntity), entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, playerEntity.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    } else {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.attackEntityFrom(DamageSource.MAGIC, entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, entityAttacked.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    }
                }
                break;

            case BLAZING_ALLOY: //Burn for longer
                if (!entityAttacked.isBurning() && !entityAttacked.isImmuneToFire()) {
                    attackedEntityWorld.playSound(null, entityAttacked.getPosition(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
                entityAttacked.setFire(10);
                break;

            case FREEZING_ALLOY: //Stronger Slow
                entityAttacked.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 60, 2, false, true));
                break;

            case ENDER_ALLOY: //Execute but higher threshold
                if (entityAttacked.getHealth() - amountDealt <= entityAttacked.getMaxHealth() * 0.3 && !attackedEntityWorld.isRemote()) {
                    if (playerEntity != null) {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.attackEntityFrom(DamageSource.causePlayerDamage(playerEntity), entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, playerEntity.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    } else {
                        if (!loopCheck) {
                            loopCheck = true;
                            entityAttacked.attackEntityFrom(DamageSource.MAGIC, entityAttacked.getMaxHealth() * 10);
                            attackedEntityWorld.playSound(null, entityAttacked.getPosition(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.8F, 1.0F);
                            loopCheck = false;
                        }
                    }
                }
                break;
        }
    }
}
