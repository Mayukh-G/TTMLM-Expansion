package com.example.examplemod.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class OreBlocks extends OreBlock {
    public OreBlocks() {
        this(Properties.create(Material.ROCK)
        .hardnessAndResistance(4f,6f)
        .sound(SoundType.STONE));
    }
    public OreBlocks(Properties builder) {
        super(builder);
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? MathHelper.nextInt(RANDOM, 1,5) : 0;
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 2;
    }

    @Nullable
    @Override // Required for Harvest LV to work
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }


}
