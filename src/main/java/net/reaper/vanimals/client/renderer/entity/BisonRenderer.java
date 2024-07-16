package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.BisonModel;
import net.reaper.vanimals.client.model.entity.GobblerModel;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.ground.GobblerEntity;

public class BisonRenderer extends MobRenderer<BisonEntity, BisonModel> {
    public BisonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BisonModel(pContext.bakeLayer(BisonModel.BISON_LAYER)), 1.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(BisonEntity pEntity) {
        return new ResourceLocation(Vanimals.MODID, (pEntity.isSaddled()?"textures/entity/bison_saddled.png":"textures/entity/bison.png"));

    }

    @Override
    public void render(BisonEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {



        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    protected void scale(BisonEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {

        float F = pLivingEntity.isBaby() ? 0.5F : 1.0F;
        pMatrixStack.scale(F, F, F);

        super.scale(pLivingEntity, pMatrixStack, pPartialTickTime);
    }

}
