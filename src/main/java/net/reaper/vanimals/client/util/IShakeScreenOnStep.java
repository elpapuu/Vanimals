package net.reaper.vanimals.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.reaper.vanimals.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface IShakeScreenOnStep {
   float getShakePower();

   default float getMaxScreenShake() {
      return 0.7F;
   }

   default boolean canEntityShake(@NotNull LivingEntity pEntity) {
      return !pEntity.isInFluidType() && pEntity.onGround();
   }

   private float calculateShakeIntensity(float pAnimationSpeed) {
      float animationFactor = 1.5F + pAnimationSpeed * 0.3F;
      return 0.2F * animationFactor;
   }

   default void handleScreenShake() {
      Player player = Minecraft.getInstance().player;
      if (player != null) {
         List<LivingEntity> entities = new ArrayList<>(EntityUtils.getEntitiesAroundPos(LivingEntity.class, player, player.position(), 8.0F, false));
         for (LivingEntity entity : entities) {
            if (this.canEntityShake(entity) && EntityUtils.isEntityStepping(entity, getShakeFrequency(), this.calculateShakeIntensity(entity.walkAnimation.speed())) && entity != player) {
               float distanceFactor = getShakeDistance() / entity.distanceTo(player) / 8.0F;
               float shakePower = Math.min(distanceFactor * getShakePower(), getMaxScreenShake());
               PlayerScreenShaker.startShake(15, shakePower);
            }
         }
      }
   }

   float getShakeFrequency();

   float getShakeDistance();
}