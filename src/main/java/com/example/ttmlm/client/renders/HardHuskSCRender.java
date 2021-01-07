package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.HardHuskSCModel;
import com.example.ttmlm.client.models.HardZombieSCModel;
import com.example.ttmlm.entity.original.HardHuskSwarmCaller;
import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HardHuskSCRender extends BipedRenderer<HardHuskSwarmCaller, HardHuskSCModel> {

    public HardHuskSCRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HardHuskSCModel(), 0.7F);
    }

    @Override
    protected void preRenderCallback(HardHuskSwarmCaller entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull HardHuskSwarmCaller entity) {
        return TTMLM.getID("textures/entity/hard_husk_sc.png");
    }
}
