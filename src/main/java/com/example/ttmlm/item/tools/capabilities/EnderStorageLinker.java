package com.example.ttmlm.item.tools.capabilities;


import com.example.ttmlm.init.IngotVariants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
    public ChestTileEntity getContainer(World world) {
        if(this.linkedTool.hasTag()){
            CompoundNBT compoundNBT = this.linkedTool.getTag().getCompound("Container");
            int x = compoundNBT.getInt("x");
            int y = compoundNBT.getInt("y");
            int z = compoundNBT.getInt("z");
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity tEntity = world.getTileEntity(pos);
            if(tEntity instanceof ChestTileEntity){
                return (ChestTileEntity) tEntity;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public ItemStack getLinkedTool() {
        return linkedTool;
    }

    @Override
    public boolean link(ChestTileEntity container) {
        if(container == this.getContainer(container.getWorld())) {
            this.unlink();
            return false;
        }
        CompoundNBT compoundNBT = container.serializeNBT();
        String worldS = container.getWorld().toString();
        if(!this.linkedTool.hasTag()){
            this.linkedTool.setTag(new CompoundNBT());
        }
        this.linkedTool.getTag().put("Container", compoundNBT);
        this.linkedTool.getTag().putString("World", worldS);
        return true;
    }

    private void unlink() {
        this.linkedTool.getTag().remove("Container");
    }
}
