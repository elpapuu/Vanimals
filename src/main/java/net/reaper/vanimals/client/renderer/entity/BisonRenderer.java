package net.reaper.vanimals.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.reaper.vanimals.common.entity.utils.ICustomPlayerRidePos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.BisonModel;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import org.jetbrains.annotations.NotNull;

public class BisonRenderer extends MobRenderer<BisonEntity, BisonModel<BisonEntity>> implements ICustomPlayerRidePos{
    public BisonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BisonModel<BisonEntity>(pContext.bakeLayer(BisonModel.BISON_LAYER)), 1.3f);
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
    @Override
    public <T extends LivingEntity> void applyRiderPose(HumanoidModel<T> pHumanoidModel, @NotNull T pRider) {
        pHumanoidModel.rightArm.xRot = this.rad(-155.0F);
        pHumanoidModel.leftArm.xRot = this.rad(-155.0F);
        pHumanoidModel.rightLeg.xRot = this.rad(5.0F);
        pHumanoidModel.rightLeg.zRot = this.rad(10.0F);
        pHumanoidModel.leftLeg.xRot = this.rad(5.0F);
        pHumanoidModel.leftLeg.zRot = this.rad(-10.0F);
        pHumanoidModel.head.xRot = this.rad(-80.0F);
        pHumanoidModel.head.yRot = Mth.clamp(pHumanoidModel.head.yRot, this.rad(-35.0F), this.rad(35.0F));
    }

    @Override
    public <T extends Entity> void applyRiderMatrixStack(@NotNull T pVehicle, @NotNull PoseStack pMatrixStack) {
        this.getModel().setMatrixStack(pMatrixStack);
        pMatrixStack.translate(0.0F, 0.05F - pVehicle.getBbHeight(), 1.7F);
        pMatrixStack.mulPose(Axis.YN.rotationDegrees(180.0F));
        pMatrixStack.mulPose(Axis.XN.rotationDegrees(295.0F));
    }

    protected void scale(BisonEntity pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime) {

        float F = pLivingEntity.isBaby() ? 0.5F : 1.0F;
        pMatrixStack.scale(F, F, F);

        super.scale(pLivingEntity, pMatrixStack, pPartialTickTime);
    }

}
