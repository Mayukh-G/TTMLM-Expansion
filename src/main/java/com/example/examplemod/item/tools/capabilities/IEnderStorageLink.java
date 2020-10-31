package com.example.examplemod.item.tools.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.world.World;

public interface IEnderStorageLink {

    ChestTileEntity getContainer(World world);

    ItemStack getLinkedTool();

    boolean link(ChestTileEntity container);

}
