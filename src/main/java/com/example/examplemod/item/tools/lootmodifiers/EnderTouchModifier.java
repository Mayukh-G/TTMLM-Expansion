package com.example.examplemod.item.tools.lootmodifiers;

import com.example.examplemod.item.tools.capabilities.ESLCapability;
import com.example.examplemod.item.tools.capabilities.EnderStorageLinker;
import com.example.examplemod.item.tools.capabilities.IEnderStorageLink;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class EnderTouchModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected EnderTouchModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        /*
            For Chest Linking, Try Creating NBT tags containing the object itself, if need be create new NBM writer and
             reader classes. REad the NBT tag with the object here and store it into a variable, from that variable you
             will have access to the inventory of that object and will be able to modify as you wish
             ^^ Above does not work well, too long and clustered, use Capabilities instead.
         */
        ItemStack toolItemStack = context.get(LootParameters.TOOL);
        LazyOptional<IEnderStorageLink> toolOptional = toolItemStack.getCapability(ESLCapability.ENDER_STORAGE_LINK_CAPABILITY, null);
        IEnderStorageLink linker = toolOptional.orElse(EnderStorageLinker.factory());
        if(linker.getContainer() != null){
            ChestTileEntity container = linker.getContainer();
            LazyOptional<IItemHandler> containerOptional = container.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            IItemHandler itemHandler = containerOptional.orElseThrow(Error::new);

            int maxSlot = itemHandler.getSlots();
            ItemStack remainder;
            int j = 0;
            for (int i = 0; i < maxSlot || generatedLoot.size() == 0; i++){
                remainder = itemHandler.insertItem(i, generatedLoot.get(i - j), false);
                if (!remainder.isEmpty()){
                    j++;
                }
            }
        }
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (entity instanceof ServerPlayerEntity){
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            PlayerInventory inv = player.inventory;
            generatedLoot.removeIf(inv::addItemStackToInventory);
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<EnderTouchModifier> {
        @Override
        public EnderTouchModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new EnderTouchModifier(conditionsIn);
        }
    }
}
