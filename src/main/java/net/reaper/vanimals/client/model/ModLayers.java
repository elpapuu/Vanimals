package net.reaper.vanimals.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.client.model.entity.BisonModel;
import net.reaper.vanimals.client.model.entity.NapoleonFishModel;

public class ModLayers {
    public static final ModelLayerLocation BISON_LAYER;
    public static final ModelLayerLocation BISON_SADDLED_LAYER;
    public static final ModelLayerLocation NAPOLEON_FISH_LAYER;

    public static ModelLayerLocation newLayer(String pName, String pLayer) {
        return new ModelLayerLocation(new ResourceLocation(Vanimals.MODID, pName + "_layer"), pLayer);
    }

    public static ModelLayerLocation newLayer(String pName) {
        return newLayer(pName, "main");
    }

    static {
        BISON_LAYER = newLayer("bison");
        BISON_SADDLED_LAYER = newLayer("bison_saddled", "second");
        NAPOLEON_FISH_LAYER = newLayer("napoleon_fish");
    }

    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions pEvent) {
        pEvent.registerLayerDefinition(BISON_LAYER, BisonModel::createBodyLayer);
        pEvent.registerLayerDefinition(NAPOLEON_FISH_LAYER, NapoleonFishModel::createBodyLayer);
    }
}
