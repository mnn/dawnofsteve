/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.network.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.relauncher.Side;
import monnef.dawn.network.DawnPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.DimensionManager;

import java.net.ProtocolException;

public class SpawnParticlePacket extends DawnPacket {
    public enum SpawnType {
        BULLET_SMOKE,
        GUNPOWDER_SMOKE
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

    public SpawnParticlePacket(SpawnType type, int dim, Vec3 vector) {
        this.type = type;
        this.dim = dim;
        this.x = vector.xCoord;
        this.y = vector.yCoord;
        this.z = vector.zCoord;
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

        EntityFX toSpawn = null;
        if (type == SpawnType.BULLET_SMOKE) {
            toSpawn = new EntitySmokeFX(DimensionManager.getWorld(dim), x, y, z, 0, 0, 0, 1.5f);
        } else if (type == SpawnType.GUNPOWDER_SMOKE) {
            toSpawn = new EntitySmokeFX(DimensionManager.getWorld(dim), x, y, z, 0, 0.001, 0, 2f);
        }

        if (toSpawn != null)
            Minecraft.getMinecraft().effectRenderer.addEffect(toSpawn);
    }
}
