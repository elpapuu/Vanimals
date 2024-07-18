package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.ShieldosteusModel;
import net.reaper.vanimals.common.entity.water.ShieldosteusEntity;

public class ShieldosteusRenderer extends MobRenderer<ShieldosteusEntity, ShieldosteusModel> {
    public ShieldosteusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ShieldosteusModel(pContext.bakeLayer(ShieldosteusModel.SHIELDOSTEUS_LAYER)), 1.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(ShieldosteusEntity pEntity) {
        return new ResourceLocation(Vanimals.MODID, "textures/entity/shieldosteus.png");

    }

    @Override
    public void render(ShieldosteusEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {



        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    protected void scale(ShieldosteusEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {

        float F = pLivingEntity.isBaby() ? 0.5F : 1.0F;
        pMatrixStack.scale(F, F, F);

        super.scale(pLivingEntity, pMatrixStack, pPartialTickTime);
    }

}
