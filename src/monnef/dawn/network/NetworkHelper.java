/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.network.packet.Packet;

public class NetworkHelper {

    public static void sendToServer(ReloadPacket.ReloadType type) {
        sendToServer(new ReloadPacket(type).makePacket());
    }

    private static void sendToServer(Packet packet) {
        PacketDispatcher.sendPacketToServer(packet);
    }
}
