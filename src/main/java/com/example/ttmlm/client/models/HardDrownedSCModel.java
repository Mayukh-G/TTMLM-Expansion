package com.example.ttmlm.client.models;

import com.example.ttmlm.entity.original.HardDrownedSwarmCaller;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import org.jetbrains.annotations.NotNull;

public class HardDrownedSCModel extends AbstractZombieModel<HardDrownedSwarmCaller> {

    public HardDrownedSCModel(){
        this(0.0F, 0.0F, 64, 64);
    }

    protected HardDrownedSCModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public boolean isAggressive(@NotNull HardDrownedSwarmCaller entityIn) {
        return entityIn.isAggressive();
    }

}
