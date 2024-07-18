package net.reaper.vanimals.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.client.model.entity.*;
import net.reaper.vanimals.client.renderer.entity.*;
import net.reaper.vanimals.core.init.ModEntities;
import net.reaper.vanimals.Vanimals;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Vanimals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BisonModel.BISON_LAYER, BisonModel::createBodyLayer);
        event.registerLayerDefinition(BisonModel.BISON_SADDLED_LAYER, BisonModel::createBodyLayer);
        event.registerLayerDefinition(CreeperfishModel.CREEPERFISH_LAYER, CreeperfishModel::createBodyLayer);
        event.registerLayerDefinition(GobblerModel.GOBBLER_LAYER, GobblerModel::createBodyLayer);
        event.registerLayerDefinition(ShieldosteusModel.SHIELDOSTEUS_LAYER, ShieldosteusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BISON.get(), BisonRenderer::new);
        event.registerEntityRenderer(ModEntities.CREEPERFISH.get(), CreeperfishRenderer::new);
        event.registerEntityRenderer(ModEntities.GOBBLER.get(), GobblerRenderer::new);
        event.registerEntityRenderer(ModEntities.SHIELDOSTEUS.get(), ShieldosteusRenderer::new);
    }
}