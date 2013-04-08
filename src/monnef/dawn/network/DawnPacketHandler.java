/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import monnef.dawn.DawnOfSteve;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.net.ProtocolException;

public class DawnPacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        try {
            EntityPlayer entityPlayer = (EntityPlayer) player;
            ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
            int packetId = in.readUnsignedByte();
            DawnPacket dawnPacket = DawnPacket.constructPacket(packetId);
            dawnPacket.read(in);
            dawnPacket.execute(entityPlayer, entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
        } catch (ProtocolException e) {
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer("Protocol Exception!");
                DawnOfSteve.Log.printWarning("Player " + ((EntityPlayer) player).username + " caused a Protocol Exception!");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unexpected Reflection exception during Packet construction!", e);
        }
    }
}
