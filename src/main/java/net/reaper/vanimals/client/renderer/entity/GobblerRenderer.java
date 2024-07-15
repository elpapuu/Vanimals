package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.GobblerModel;
import net.reaper.vanimals.common.entity.ground.GobblerEntity;

public class GobblerRenderer extends MobRenderer<GobblerEntity, GobblerModel> {
    public GobblerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GobblerModel(pContext.bakeLayer(GobblerModel.GOBBLER_LAYER)), 1.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(GobblerEntity pEntity) {
        return new ResourceLocation(Vanimals.MODID, "textures/entity/gobbler.png");
    }

    @Override
    public void render(GobblerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {



        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

}