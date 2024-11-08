package net.reaper.vanimals.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.reaper.vanimals.Vanimals;
import net.reaper.vanimals.common.network.packet_builder.CorePacket;
import org.jetbrains.annotations.Nullable;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final ResourceLocation PACKET_NETWORK_NAME = new ResourceLocation(Vanimals.MODID + ":main_channel");
    public static final SimpleChannel NETWORK_WRAPPER = NetworkRegistry
            .ChannelBuilder.named(PACKET_NETWORK_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void registerPackets() {
        NETWORK_WRAPPER.registerMessage(1, CorePacket.class, CorePacket::write, CorePacket::read, CorePacket::handle);
    }

    public static <MSG> void sendMSGToServer(MSG pMessage) {
        NETWORK_WRAPPER.sendToServer(pMessage);
    }

    public static <M> void sendToPlayer(M pMessage, Player pPlayer) {
        if (pPlayer instanceof ServerPlayer serverPlayer) {
            NETWORK_WRAPPER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), pMessage);
        }
    }
}
