package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.HardZombieSCModel;
import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HardZombieSCRender extends BipedRenderer<HardZombieSwarmCaller, HardZombieSCModel> {

    public HardZombieSCRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HardZombieSCModel(), 0.7F);
    }

    @Override
    protected void preRenderCallback(HardZombieSwarmCaller entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull HardZombieSwarmCaller entity) {
        return TTMLM.getID("textures/entity/hard_zombie_sc.png");
    }
}
