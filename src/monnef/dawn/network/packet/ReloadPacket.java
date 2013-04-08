/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import monnef.dawn.network.DawnPacket;
import net.minecraft.entity.player.EntityPlayer;

public class ReloadPacket extends DawnPacket {
    public enum ReloadType {
        CS_WANT_RELOAD,
        SC_START_RELOADING,
        SC_RELOADED,
        CS_STOPPED
    }

    private ReloadType type;

    public ReloadPacket() {
    }

    public ReloadPacket(ReloadType type) {
        this.type = type;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(type.ordinal());
    }

    @Override
    public void read(ByteArrayDataInput in) {
        type = ReloadType.values()[in.readInt()];
    }

    @Override
    public void execute(EntityPlayer player, Side side) {
        player.addChatMessage((side.isClient() ? "C" : "S") + " rld pck");
    }
}
