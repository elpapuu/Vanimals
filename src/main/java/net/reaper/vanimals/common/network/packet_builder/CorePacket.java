package net.reaper.vanimals.common.network.packet_builder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CorePacket {
    private int packetId;
    private DataType[] dataTypes;
    private Object[] data;
    private Side side;

    public CorePacket(int pPacketId, Side pSide, DataType[] pDataTypes, Object[] pData) {
        this.packetId = pPacketId;
        this.side = pSide;
        this.dataTypes = pDataTypes;
        this.data = pData;
    }

    public CorePacket() {}

    public static CorePacket read(FriendlyByteBuf pBuf) {
        int id = pBuf.readInt();
        int typeCount = pBuf.readInt();
        DataType[] dataTypes = new DataType[typeCount];
        Object[] data = new Object[typeCount];
        Side side = Side.values()[pBuf.readInt()];
        for (int i = 0; i < typeCount; i++) {
            dataTypes[i] = DataType.fromOrdinal(pBuf.readInt());
            data[i] = dataTypes[i].decode(pBuf);
        }
        return new CorePacket(id, side, dataTypes, data);
    }

    public static void write(CorePacket pMessage, FriendlyByteBuf pBuf) {
        pBuf.writeInt(pMessage.packetId);
        pBuf.writeInt(pMessage.dataTypes.length);
        pBuf.writeInt(pMessage.side.ordinal());
        for (int i = 0; i < pMessage.dataTypes.length; i++) {
            pBuf.writeInt(pMessage.dataTypes[i].ordinal());
            pMessage.dataTypes[i].encode(pBuf, pMessage.data[i]);
        }
    }

    public static void handle(CorePacket pMessage, Supplier<NetworkEvent.Context> pContext) {
        NetworkEvent.Context context = pContext.get();
        context.enqueueWork(() -> {
            if ((pMessage.side == Side.SERVER && context.getDirection().getReceptionSide() == LogicalSide.SERVER) || (pMessage.side == Side.CLIENT && context.getDirection().getReceptionSide() == LogicalSide.CLIENT)) {
                PacketProcessor.process(pMessage.packetId, pMessage.data, context);
            }
        });
        context.setPacketHandled(true);
    }
}