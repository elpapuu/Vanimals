package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.reaper.vanimals.client.model.ModLayers;
import net.reaper.vanimals.client.model.entity.NapoleonFishModel;
import net.reaper.vanimals.client.util.ResourceUtils;
import net.reaper.vanimals.common.entity.water.NapoleonFishEntity;
import org.jetbrains.annotations.NotNull;

public class NapoleonFishRenderer extends MobRenderer<NapoleonFishEntity, NapoleonFishModel> {
    public NapoleonFishRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NapoleonFishModel(pContext.bakeLayer(ModLayers.NAPOLEON_FISH_LAYER)), 1.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull NapoleonFishEntity pEntity) {
        return ResourceUtils.entity("napoleon_fish/napoleon_fish_" + NapoleonFishEntity.EMOTION.get(pEntity));
    }

    @Override
    protected void scale(NapoleonFishEntity pEntity, PoseStack pMatrixStack, float pPartialTick) {
        float size = pEntity.isBaby() ? 0.5F : 1.0F;
        pMatrixStack.scale(size, size, size);
        super.scale(pEntity, pMatrixStack, pPartialTick);
    }
}
