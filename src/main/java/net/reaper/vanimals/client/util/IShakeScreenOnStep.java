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
      return !pEntity.isInFluidType() && pEntity.onGround() && pEntity.isAlive();
   }

   float getShakeFrequency();

   float getShakeDistance();
}