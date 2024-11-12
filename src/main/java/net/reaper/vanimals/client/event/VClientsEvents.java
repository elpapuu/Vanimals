package net.reaper.vanimals.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.client.VClientProxy;
import net.reaper.vanimals.client.input.InputKey;
import net.reaper.vanimals.client.input.InputStateManager;
import net.reaper.vanimals.client.input.KeyPressType;
import net.reaper.vanimals.client.util.IDynamicCamera;
import net.reaper.vanimals.client.util.ICustomPlayerRidePos;
import net.reaper.vanimals.client.util.IShakeScreenOnStep;
import net.reaper.vanimals.client.util.PlayerScreenShaker;
import net.reaper.vanimals.common.network.NetworkHandler;
import net.reaper.vanimals.common.network.packet_builder.CorePacket;
import net.reaper.vanimals.common.network.packet_builder.DataType;
import net.reaper.vanimals.common.network.packet_builder.Side;
import net.reaper.vanimals.common.util.RenderUtil;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.util.EntityUtils;

@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class VClientsEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent pEvent) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.level().isClientSide()) {
                if (pEvent.phase == TickEvent.Phase.END) {
                    player.level().getEntities(player, player.getBoundingBox().inflate(15.0F), entity -> {
                        return entity instanceof IShakeScreenOnStep && entity.isAlive();
                    }).forEach(entity -> {
                        if (entity instanceof IShakeScreenOnStep screenShaker) {
                            if (screenShaker.canEntityShake((LivingEntity) entity)) {
                                float shakePower = screenShaker.getShakePower() * (1.0F - (player.distanceTo(entity) / screenShaker.getShakeDistance()));
                                if (shakePower > 0.0F) {
                                    PlayerScreenShaker.startShake(15, shakePower);
                                }
                            }
                        }
                    });
                }
                if (pEvent.phase == TickEvent.Phase.START) {
                    InputStateManager.getInstance().update();
                    for (InputKey inputKey : InputKey.values()) {
                        for (KeyPressType pressType : KeyPressType.values()) {
                            if (InputStateManager.getInstance().isKeyPress(inputKey, pressType)) {
                                CorePacket packet = new CorePacket(1, Side.SERVER, new DataType[]{DataType.INTEGER, DataType.INTEGER}, new Object[]{inputKey.ordinal(), pressType.ordinal()});
                                NetworkHandler.sendMSGToServer(packet);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key pEvent) {
        InputKey inputKey = InputKey.fromKeyCode(pEvent.getKey());
        if (inputKey != null) {
            InputStateManager.getInstance().updateKeyState(inputKey, pEvent.getAction() != 0);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onComputeCameraAngle(ViewportEvent.ComputeCameraAngles pEvent) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.isPassenger()) {
                Entity vehicle = player.getRootVehicle();
                if (vehicle instanceof Mob mob) {
                    if (vehicle instanceof IDynamicCamera dynamicCamera) {
                        float targetRoll = Math.max(-dynamicCamera.getMaxCameraTilt(), Math.min(dynamicCamera.getMaxCameraTilt(), (mob.yBodyRot -mob.yBodyRotO) * dynamicCamera.getCameraTiltSpeed()));
                        VClientProxy.currentRoll = VClientProxy.currentRoll + (targetRoll - VClientProxy.currentRoll) * 0.1F;
                        pEvent.setRoll(pEvent.getRoll() - VClientProxy.currentRoll);
                    } else {
                        VClientProxy.currentRoll = VClientProxy.currentRoll + (0.0F - VClientProxy.currentRoll) * 0.1F;
                        pEvent.setRoll(pEvent.getRoll() - VClientProxy.currentRoll);
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