/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.common;

import cpw.mods.fml.common.FMLCommonHandler;
import monnef.dawn.network.packet.SpawnParticlePacket;

public class CommonProxy {
    public boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public boolean isClient() {
        return !isServer();
    }

    public String getSideString() {
        return isServer() ? "S" : "C";
    }

    public void onLoad() {
    }

    public void onPreLoad() {
    }

    public int addArmor(String name) {
        return 0;
    }

    public void spawnParticle(SpawnParticlePacket packet) {

    }
}
