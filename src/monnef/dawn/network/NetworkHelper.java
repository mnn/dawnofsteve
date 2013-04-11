/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;

public class NetworkHelper {

    public static void sendToServer(ReloadPacket.ReloadType type) {
        sendToServer(new ReloadPacket(type).makePacket());
    }

    public static void sendToServer(Packet packet) {
        PacketDispatcher.sendPacketToServer(packet);
    }

    public static void sendToAllAround(Entity entity, double range, Packet packet) {
        sendToAllAround(entity.posX, entity.posY, entity.posZ, entity.dimension, range, packet);
    }

    public static void sendToAllAround(double x, double y, double z, int dim, double range, Packet packet) {
        PacketDispatcher.sendPacketToAllAround(x, y, z, range, dim, packet);
    }
}
