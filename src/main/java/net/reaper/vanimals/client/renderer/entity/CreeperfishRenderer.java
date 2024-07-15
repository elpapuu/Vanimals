package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.BisonModel;
import net.reaper.vanimals.client.model.entity.CreeperfishModel;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import net.reaper.vanimals.common.entity.water.CreeperfishEntity;

public class CreeperfishRenderer extends MobRenderer<CreeperfishEntity, CreeperfishModel> {
    public CreeperfishRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperfishModel(pContext.bakeLayer(CreeperfishModel.CREEPERFISH_LAYER)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperfishEntity pEntity) {
        return new ResourceLocation(Vanimals.MODID, "textures/entity/creeperfish.png");
    }

    @Override
    public void render(CreeperfishEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {



        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

}
