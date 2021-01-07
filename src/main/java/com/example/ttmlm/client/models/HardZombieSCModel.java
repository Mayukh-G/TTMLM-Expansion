package com.example.ttmlm.client.models;

import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import org.jetbrains.annotations.NotNull;

public class HardZombieSCModel extends AbstractZombieModel<HardZombieSwarmCaller> {

    public HardZombieSCModel(){
        this(0.0F, 0.0F, 64, 64);
    }

    protected HardZombieSCModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public boolean isAggressive(@NotNull HardZombieSwarmCaller entityIn) {
        return entityIn.isAggressive();
    }

}
