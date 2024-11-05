package net.reaper.vanimals.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.client.ModClientProxy;
import net.reaper.vanimals.client.util.IDynamicCamera;
import net.reaper.vanimals.client.util.ICustomPlayerRidePos;
import net.reaper.vanimals.common.util.RenderUtil;
import net.reaper.vanimals.Vanimals;

@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onComputeCameraAngle(ViewportEvent.ComputeCameraAngles pEvent) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.isPassenger()) {
                Entity vehicle = player.getRootVehicle();
                if (vehicle instanceof Mob mob) {
                    if (vehicle instanceof IDynamicCamera dynamicCamera) {
                        float targetRoll = Math.max(-dynamicCamera.getMaxCameraTilt(), Math.min(dynamicCamera.getMaxCameraTilt(), (mob.yBodyRot -mob.yBodyRotO) * dynamicCamera.getCameraTiltSpeed()));
                        ModClientProxy.currentRoll = ModClientProxy.currentRoll + (targetRoll - ModClientProxy.currentRoll) * 0.1F;
                        pEvent.setRoll(pEvent.getRoll() - ModClientProxy.currentRoll);
                    } else {
                        ModClientProxy.currentRoll = ModClientProxy.currentRoll + (0.0F - ModClientProxy.currentRoll) * 0.1F;
                        pEvent.setRoll(pEvent.getRoll() - ModClientProxy.currentRoll);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static <T extends LivingEntity> void onPlayerPose(PlayerPoseEvent<T> pEvent) {
        T entity = pEvent.getEntity();
        if (entity instanceof Player) {
            if (entity.getVehicle() instanceof Mob mob) {
                T vehicle = (T) mob;
                if (RenderUtil.getEntityRenderer(vehicle) instanceof ICustomPlayerRidePos customRidePos) {
                    customRidePos.applyRiderPose(vehicle, pEvent.getHumanoidModel(), entity);
                }
            }
        }
    }

    @SubscribeEvent
    public static <T extends LivingEntity> void onModelRotation(ModelRotationEvent<T> pEvent) {
        T entity = pEvent.getEntity();
        if (entity instanceof Player) {
            pEvent.setCanceled(RenderUtil.getEntityRenderer(entity.getVehicle()) instanceof ICustomPlayerRidePos);
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    @SubscribeEvent
    public static void onPrePlayerRender(RenderPlayerEvent.Pre pEvent) {
        Player player = pEvent.getEntity();
        RenderPlayerEvent.Post event = new RenderPlayerEvent.Post(player, pEvent.getRenderer(), pEvent.getPartialTick(), pEvent.getPoseStack(), pEvent.getMultiBufferSource(), pEvent.getPackedLight());
        synchronized (RenderUtil.hiddenEntities) {
            if (RenderUtil.hiddenEntities.remove(player.getUUID()) && RenderUtil.shouldSkipRendering(false, Minecraft.getInstance().getCameraEntity())) {
                MinecraftForge.EVENT_BUS.post(event);
                pEvent.setCanceled(true);
            }
        }
    }
}