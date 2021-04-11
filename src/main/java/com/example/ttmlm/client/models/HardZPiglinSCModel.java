package com.example.ttmlm.client.models;

import com.example.ttmlm.entity.original.HardZPiglinSwarmCaller;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import org.jetbrains.annotations.NotNull;

public class HardZPiglinSCModel extends AbstractZombieModel<HardZPiglinSwarmCaller> {

    public HardZPiglinSCModel(){
        this(0.0F, 0.0F, 64, 64);
    }

    protected HardZPiglinSCModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public boolean isAggressive(@NotNull HardZPiglinSwarmCaller entityIn) {
        return entityIn.isAggressive();
    }

}
