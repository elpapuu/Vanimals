package net.reaper.vanimals.client.util;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.Vanimals;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerScreenShaker {
    public static double shakeTicks = 0.0F;
    public static double shakePower = 0.0F;

    public static void startShake(int pDuration, float pShakePower) {
        shakeTicks = pDuration;
        shakePower = Math.min(pShakePower, 5.0F);
    }

    @SubscribeEvent
    public static void onUpdateScreenShaker(TickEvent.ClientTickEvent pEvent) {
        if (pEvent.phase == TickEvent.Phase.START) {
            if (!Minecraft.getInstance().isPaused()) {
                if (shakeTicks > 0.0F) {
                    shakeTicks -= 5.0F;
                } else {
                    shakePower = 0.0F;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onUpdateCamera(ViewportEvent.ComputeCameraAngles pEvent) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (!Minecraft.getInstance().isPaused()) {
                if (shakeTicks > 0.0F) {
                    double intensity = shakePower * Minecraft.getInstance().options.screenEffectScale().get();
                    double maxShakeTicks = 20.0;
                    double damping = Math.max(0.0, shakeTicks / maxShakeTicks);
                    double amplitude = intensity * damping;
                    double frequency = 1.0;
                    double offsetX = amplitude * Math.sin(player.tickCount * frequency + player.getRandom().nextDouble() * Math.PI);
                    double offsetY = amplitude * Math.cos(player.tickCount * frequency + player.getRandom().nextDouble() * Math.PI * 0.5);
                    double offsetZ = amplitude * Math.sin(player.tickCount * frequency + player.getRandom().nextDouble() * Math.PI * 0.25);
                    try {
                        Method moveMethod = Camera.class.getDeclaredMethod("move", double.class, double.class, double.class);
                        moveMethod.setAccessible(true);
                        moveMethod.invoke(pEvent.getCamera(), offsetX, offsetY, offsetZ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
