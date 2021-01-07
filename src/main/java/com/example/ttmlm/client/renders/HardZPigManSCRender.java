package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.HardDrownedSCModel;
import com.example.ttmlm.client.models.HardZPigManSCModel;
import com.example.ttmlm.client.models.HardZombieSCModel;
import com.example.ttmlm.entity.original.HardDrownedSwarmCaller;
import com.example.ttmlm.entity.original.HardZPigManSwarmCaller;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HardZPigManSCRender extends BipedRenderer<HardZPigManSwarmCaller, HardZPigManSCModel> {

    public HardZPigManSCRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new HardZPigManSCModel(), 0.7F);
    }

    @Override
    protected void preRenderCallback(HardZPigManSwarmCaller entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull HardZPigManSwarmCaller entity) {
        return TTMLM.getID("textures/entity/hard_z_pigman_sc.png");
    }
}
