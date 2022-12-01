package com.example.ttmlm.client.renders;

import com.example.ttmlm.TTMLM;
import com.example.ttmlm.client.models.HardZombieSCModel;
import com.example.ttmlm.entity.original.HardZombieSwarmCaller;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    public void render(HardZombieSwarmCaller p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        p_225623_4_.scale(1.2F, 1.2F, 1.2F);
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull HardZombieSwarmCaller entity) {
        return TTMLM.getID("textures/entity/hard_zombie_sc.png");
    }
}
