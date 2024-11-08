package net.reaper.vanimals.common.network.packet_builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.network.NetworkEvent;
import net.reaper.vanimals.client.input.InputKey;
import net.reaper.vanimals.client.input.KeyPressType;
import net.reaper.vanimals.common.entity.ground.BisonEntity;
import org.jline.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketProcessor {
    private static final Map<Integer, PacketHandler> handlers = new HashMap<>();

    public static void registerHandler(int pPacketId, PacketHandler pHandler) {
        handlers.put(pPacketId, pHandler);
    }

    public static void registerHandlers() {
        PacketProcessor.registerHandler(1, (pPacketId, pData, pContext) -> {
            InputKey inputKey = InputKey.values()[(int) pData[0]];
            KeyPressType keyPressType = KeyPressType.values()[(int) pData[1]];
            ServerPlayer player = pContext.getSender();
            if (player != null) {
                if (player.isPassenger()) {
                    if (player.getRootVehicle() instanceof BisonEntity rideableAnimal) {
                        rideableAnimal.onServerInput(inputKey, keyPressType);
                    }
                }
            }
        });
    }

    public static void process(int pPacketId, Object[] pData, NetworkEvent.Context pContext) {
        PacketHandler handler = handlers.get(pPacketId);
        if (handler != null) {
            handler.handle(pPacketId, pData, pContext);
        } else {
            Log.error("No handler registered for packetId: " + pPacketId);
        }
    }
}
