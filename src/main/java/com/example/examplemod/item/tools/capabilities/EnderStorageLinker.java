package com.example.examplemod.item.tools.capabilities;


import com.example.examplemod.init.IngotVariants;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class EnderStorageLinker implements IEnderStorageLink, ICapabilityProvider {

    @Nonnull
    private ItemStack linkedTool;
    private ChestTileEntity linkedContainer;
    private static Logger LOGGER = LogManager.getLogger();

    private final LazyOptional<IEnderStorageLink> holder = LazyOptional.of(() -> this);

    public EnderStorageLinker(@NotNull ItemStack stack){
        this.linkedTool = stack;
    }

    public static EnderStorageLinker factory(){
        return new EnderStorageLinker(new ItemStack(IngotVariants.BLAZING_ALLOY.getPickaxeItem()));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return ESLCapability.ENDER_STORAGE_LINK_CAPABILITY.orEmpty(cap, holder);
    }

    @Override
    public ChestTileEntity getContainer() {
        return linkedContainer;
    }

    @NotNull
    @Override
    public ItemStack getLinkedTool() {
        return linkedTool;
    }

    @Override
    public void link(ChestTileEntity container) {
        this.linkedContainer = container;
    }
}