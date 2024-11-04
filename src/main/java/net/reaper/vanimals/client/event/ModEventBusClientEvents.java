package net.reaper.vanimals.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.client.model.entity.*;
import net.reaper.vanimals.client.renderer.entity.*;
import net.reaper.vanimals.common.entity.utils.ICustomPlayerRidePos;
import net.reaper.vanimals.common.util.RenderUtil;
import net.reaper.vanimals.core.init.ModEntities;
import net.reaper.vanimals.Vanimals;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BisonModel.BISON_LAYER, BisonModel::createBodyLayer);
        event.registerLayerDefinition(BisonModel.BISON_SADDLED_LAYER, BisonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BISON.get(), BisonRenderer::new);
    }

    @SubscribeEvent
    public static <T extends LivingEntity> void onModelRotation(ModelRotationEvent<T> pEvent) {
        T entity = pEvent.getEntity();
        if (entity instanceof Player) {
            pEvent.setCanceled(RenderUtil.getEntityRenderer(entity.getVehicle()) instanceof ICustomPlayerRidePos);
        }
    }

    @SubscribeEvent
    public static <T extends LivingEntity> void onPlayerPose(PlayerPoseEvent<T> pEvent) {
        T entity = pEvent.getEntity();
        if (entity instanceof Player) {
            if (RenderUtil.getEntityRenderer(entity.getVehicle()) instanceof ICustomPlayerRidePos customRidePos) {
                customRidePos.applyRiderPose(pEvent.getHumanoidModel(), entity);
            }
        }
    }
}