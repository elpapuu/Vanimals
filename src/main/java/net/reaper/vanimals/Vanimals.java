package net.reaper.vanimals;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.reaper.vanimals.client.VClientProxy;
import net.reaper.vanimals.client.model.ModLayers;
import net.reaper.vanimals.common.VCommonProxy;
import net.reaper.vanimals.common.network.NetworkHandler;
import net.reaper.vanimals.common.network.packet_builder.PacketProcessor;
import net.reaper.vanimals.core.init.VCreativeModTabs;
import net.reaper.vanimals.core.init.VEntityTypes;
import net.reaper.vanimals.core.init.VItems;
import net.reaper.vanimals.core.init.VSoundEvents;

@Mod(Vanimals.MODID)
public class Vanimals {
    public static final String MODID = "vanimals";
    public static VCommonProxy PROXY = DistExecutor.safeRunForDist(() -> VClientProxy::new, () -> VCommonProxy::new);

    public Vanimals() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        VCreativeModTabs.CREATIVE_MODE_TABS.register(bus);
        VItems.ITEMS.register(bus);
        VEntityTypes.ENTITY_TYPES.register(bus);
        VSoundEvents.SOUND_EVENTS.register(bus);
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        bus.addListener(ModLayers::onRegisterLayerDefinitions);
        PROXY.commonInitialize();
        PacketProcessor.registerHandlers();
    }

    private void onCommonSetup(FMLCommonSetupEvent pEvent) {
        NetworkHandler.registerPackets();
    }

    private void onClientSetup(FMLClientSetupEvent pEvent) {
        pEvent.enqueueWork(() -> {
            PROXY.clientInitialize();
        });
    }
}