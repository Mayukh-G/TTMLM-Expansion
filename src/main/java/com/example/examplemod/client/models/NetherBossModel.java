package com.example.examplemod.client.models;

import com.example.examplemod.entity.original.NetherBoss;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import org.jetbrains.annotations.NotNull;

public class NetherBossModel extends AbstractZombieModel<NetherBoss> {

    public NetherBossModel(){
        this(0.0F, 0.0F, 64, 64);
    }

    protected NetherBossModel(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    @Override
    public boolean isAggressive(@NotNull NetherBoss entityIn) {
        return entityIn.isAggressive();
    }
}
