package net.reaper.vanimals.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface ICustomPlayerRidePos  {
    default <T extends LivingEntity> void applyRiderPose(@NotNull LivingEntity pVehicle, HumanoidModel<T> pHumanoidModel, @NotNull T pRider) {

    }

    default <T extends Entity> void applyRiderMatrixStack(@NotNull T pVehicle, PoseStack pMatrixStack) {

    }

    default float rad(float f) {
        return (float) Math.toRadians(f);
    }
}