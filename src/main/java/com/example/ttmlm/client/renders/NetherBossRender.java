package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.NetherBossModel;
import com.example.ttmlm.entity.original.NetherBoss;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NetherBossRender extends BipedRenderer<NetherBoss, NetherBossModel> {

    public NetherBossRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new NetherBossModel(), 1.0F);
    }

    @Override
    protected void preRenderCallback(NetherBoss entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
    }

    @NotNull
    @Override
    public ResourceLocation getEntityTexture(@NotNull NetherBoss entity) {
        return TTMLM.getID("textures/entity/nether_boss.png");
    }
}
