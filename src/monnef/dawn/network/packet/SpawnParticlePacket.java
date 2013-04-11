/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import monnef.dawn.network.DawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

import java.net.ProtocolException;

public class SpawnParticlePacket extends DawnPacket {
    public enum SpawnType {
        BULLET_SMOKE
    }

    private SpawnType type;
    private int dim;
    private double x;
    private double y;
    private double z;

    public SpawnParticlePacket() {
    }

    public SpawnParticlePacket(SpawnType type, int dim, double x, double y, double z) {
        this.type = type;
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(type.ordinal());
        out.writeInt(dim);
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        type = SpawnType.values()[in.readInt()];
        dim = in.readInt();
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }

    @Override
    public void execute(EntityPlayer player, Side side) throws ProtocolException {
        if (side.isServer()) {
            throw new ProtocolException();
        }
        player.addChatMessage("particle spawn " + x + " " + y + " " + z);
        DimensionManager.getWorld(dim).spawnParticle("smoke", x, y, z, 0.0D, 0.0D, 0.0D);
    }
}
