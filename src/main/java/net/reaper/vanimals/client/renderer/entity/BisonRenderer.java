package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.reaper.vanimals.client.model.ModLayers;
import net.reaper.vanimals.client.renderer.entity.layer.ModPassengerLayer;
import net.reaper.vanimals.client.util.ICustomPlayerRidePos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.BisonModel;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import org.jetbrains.annotations.NotNull;

public class BisonRenderer extends MobRenderer<BisonEntity, BisonModel> implements ICustomPlayerRidePos {
    public BisonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BisonModel(pContext.bakeLayer(ModLayers.BISON_LAYER)), 1.3f);
        this.addLayer(new ModPassengerLayer<>(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BisonEntity pEntity) {
        return new ResourceLocation(Vanimals.MODID, (pEntity.isSaddled()? "textures/entity/bison_saddled.png" : "textures/entity/bison.png"));
    }

    @Override
    public <T extends LivingEntity> void applyRiderPose(@NotNull LivingEntity pVehicle, HumanoidModel<T> pHumanoidModel, @NotNull T pRider) {
        pHumanoidModel.head.yRot = Mth.clamp(pHumanoidModel.head.yRot, this.rad(-35.0F), this.rad(35.0F));
    }

    @Override
    public <T extends Entity> void applyRiderMatrixStack(@NotNull T pVehicle, @NotNull PoseStack pMatrixStack) {
        this.getModel().setMatrixStack(pMatrixStack);
        pMatrixStack.translate(0.0F, 1.25F - pVehicle.getBbHeight(), -1.0F);
        pMatrixStack.mulPose(Axis.YN.rotationDegrees(180.0F));
        pMatrixStack.mulPose(Axis.XN.rotationDegrees(180.0F));
    }

    @Override
    protected void scale(BisonEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTick) {
        float size = pLivingEntity.isBaby() ? 0.5F : 1.0F;
        pMatrixStack.scale(size, size, size);
        super.scale(pLivingEntity, pMatrixStack, pPartialTick);
    }
}
