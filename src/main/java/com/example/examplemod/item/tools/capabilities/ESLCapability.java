package com.example.examplemod.item.tools.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class ESLCapability {

    @CapabilityInject(IEnderStorageLink.class)
    public static Capability<IEnderStorageLink> ENDER_STORAGE_LINK_CAPABILITY = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(
                IEnderStorageLink.class,
                new ESLCapability.Storage(),
                EnderStorageLinker::factory);
    }

    private static class Storage implements Capability.IStorage<IEnderStorageLink>{
        @Nullable
        @Override
        public INBT writeNBT(Capability<IEnderStorageLink> capability, IEnderStorageLink instance, @Nullable Direction side) {
             return null;
        }

        @Override
        public void readNBT(Capability<IEnderStorageLink> capability, IEnderStorageLink instance, Direction side, INBT nbt) {

        }
    }
}
