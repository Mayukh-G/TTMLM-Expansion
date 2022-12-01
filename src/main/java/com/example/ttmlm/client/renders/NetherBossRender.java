package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.NetherBossModel;
import com.example.ttmlm.entity.original.NetherBoss;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    public void render(NetherBoss p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.scale(2.0F, 2.0F, 2.0F);
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull NetherBoss entity) {
        return TTMLM.getID("textures/entity/nether_boss.png");
    }
}
