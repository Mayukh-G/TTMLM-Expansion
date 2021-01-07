package com.example.ttmlm.client.models;

import com.example.ttmlm.entity.original.HardDrownedSwarmCaller;
import com.example.ttmlm.entity.original.HardZPigManSwarmCaller;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import org.jetbrains.annotations.NotNull;

public class HardZPigManSCModel extends AbstractZombieModel<HardZPigManSwarmCaller> {

    public HardZPigManSCModel(){
        this(0.0F, 0.0F, 64, 64);
    }

    protected HardZPigManSCModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public boolean isAggressive(@NotNull HardZPigManSwarmCaller entityIn) {
        return entityIn.isAggressive();
    }

}
