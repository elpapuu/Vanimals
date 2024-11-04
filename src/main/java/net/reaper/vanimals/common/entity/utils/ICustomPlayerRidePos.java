package net.reaper.vanimals.common.entity.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface ICustomPlayerRidePos  {
    default <T extends LivingEntity> void applyRiderPose(HumanoidModel<T> pHumanoidModel, @NotNull T pRider) {

    }

    default <T extends Entity> void applyRiderMatrixStack(@NotNull T pEntity, @NotNull PoseStack pMatrixStack) {

    }

    default float rad(float f) {
        return (float) Math.toRadians(f);
    }
}