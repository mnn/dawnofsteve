/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import monnef.dawn.common.Reference;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import java.net.ProtocolException;

public abstract class DawnPacket {
    private static final BiMap<Integer, Class<? extends DawnPacket>> idMap;
    public static String CHANNEL = Reference.CHANNEL;

    static {
        ImmutableBiMap.Builder<Integer, Class<? extends DawnPacket>> builder = ImmutableBiMap.builder();

        builder.put(0, ReloadPacket.class);

        idMap = builder.build();
    }

    public abstract void write(ByteArrayDataOutput out);

    public abstract void read(ByteArrayDataInput in);

    public abstract void execute(EntityPlayer player, Side side);

    public static DawnPacket constructPacket(int packetId) throws ProtocolException, IllegalAccessException, InstantiationException {
        Class<? extends DawnPacket> clazz = idMap.get(Integer.valueOf(packetId));
        if (clazz == null) {
            throw new ProtocolException("Unknown Packet Id!");
        } else {
            return clazz.newInstance();
        }
    }

    public final int getPacketId() {
        if (idMap.inverse().containsKey(getClass())) {
            return idMap.inverse().get(getClass()).intValue();
        } else {
            throw new RuntimeException("Packet " + getClass().getSimpleName() + " is missing a mapping!");
        }
    }

    public final Packet makePacket() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeByte(getPacketId());
        write(out);
        return PacketDispatcher.getPacket(CHANNEL, out.toByteArray());
    }
}
