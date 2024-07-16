package net.reaper.vanimals.common.entity.utils;

import net.minecraft.world.entity.LivingEntity;

public interface ShakeScreenOnStep {
   float getShakePower();

   default float getMaxScreenShake() {
      return 0.7F;
   }

   default boolean entityCanShake(LivingEntity pLivingEntity) {
      return !pLivingEntity.isInFluidType() && pLivingEntity.isInPowderSnow;
   }

   float getShakeFrequency();

   float getShakeDistance();
}