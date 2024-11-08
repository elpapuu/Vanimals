package net.reaper.vanimals.common.network.packet_builder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public enum DataType {
    INTEGER {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeInt((Integer) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readInt();
        }
    },
    STRING {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeUtf((String) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readUtf();
        }
    },
    BOOLEAN {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeBoolean((Boolean) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readBoolean();
        }
    },
    FLOAT {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeFloat((Float) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readFloat();
        }
    },
    DOUBLE {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeDouble((Double) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readDouble();
        }
    },
    LONG {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeLong((Long) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readLong();
        }
    },
    SHORT {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeShort((Short) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readShort();
        }
    },
    BYTE {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            pBuf.writeByte((Byte) pData);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readByte();
        }
    },
    STRING_INTEGER_MAP {
        @SuppressWarnings("unchecked")
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            Map<String, Integer> pMap = (Map<String, Integer>) pData;
            pBuf.writeInt(pMap.size());
            for (Map.Entry<String, Integer> pEntry : pMap.entrySet()) {
                pBuf.writeUtf(pEntry.getKey());
                pBuf.writeInt(pEntry.getValue());
            }
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            int pSize = pBuf.readInt();
            Map<String, Integer> pMap = new HashMap<>(pSize);
            for (int i = 0; i < pSize; i++) {
                String pKey = pBuf.readUtf();
                Integer pValue = pBuf.readInt();
                pMap.put(pKey, pValue);
            }
            return pMap;
        }
    },
    BLOCK_POS {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            BlockPos pPos = (BlockPos) pData;
            pBuf.writeInt(pPos.getX());
            pBuf.writeInt(pPos.getY());
            pBuf.writeInt(pPos.getZ());
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            int pX = pBuf.readInt();
            int pY = pBuf.readInt();
            int pZ = pBuf.readInt();
            return new BlockPos(pX, pY, pZ);
        }
    },
    VEC3 {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            Vec3 pVec = (Vec3) pData;
            pBuf.writeDouble(pVec.x);
            pBuf.writeDouble(pVec.y);
            pBuf.writeDouble(pVec.z);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            double pX = pBuf.readDouble();
            double pY = pBuf.readDouble();
            double pZ = pBuf.readDouble();
            return new Vec3(pX, pY, pZ);
        }
    },
    COMPOUND_TAG {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            CompoundTag pTag = (CompoundTag) pData;
            pBuf.writeNbt(pTag);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readNbt();
        }
    },
    COLOR {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            Color pColor = (Color) pData;
            pBuf.writeInt(pColor.getRGB());
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return new Color(pBuf.readInt());
        }
    },
    GENERIC_OBJECT {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(pData);
                byte[] objectBytes = bos.toByteArray();
                pBuf.writeInt(objectBytes.length);
                pBuf.writeBytes(objectBytes);
            } catch (IOException e) {
                throw new RuntimeException("Error encoding generic object", e);
            }
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            int length = pBuf.readInt();
            byte[] objectBytes = new byte[length];
            pBuf.readBytes(objectBytes);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error decoding generic object", e);
            }
        }
    },
    ITEM_STACK {
        @Override
        public void encode(FriendlyByteBuf pBuf, Object pData) {
            ItemStack pStack = (ItemStack) pData;
            pBuf.writeItem(pStack);
        }

        @Override
        public Object decode(FriendlyByteBuf pBuf) {
            return pBuf.readItem();
        }
    };


    public abstract void encode(FriendlyByteBuf pBuf, Object pData);

    public abstract Object decode(FriendlyByteBuf pBuf);

    public static DataType fromOrdinal(int pOrdinal) {
        return values()[pOrdinal];
    }
}
