package com.example.examplemod.item.tools.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;

public interface IEnderStorageLink {

    ChestTileEntity getContainer();

    ItemStack getLinkedTool();

    void link(ChestTileEntity container);

}
