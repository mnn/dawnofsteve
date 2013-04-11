/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import monnef.core.Reference;
import monnef.dawn.DawnOfSteve;
import monnef.dawn.network.packet.SpawnParticlePacket;

import java.util.EnumSet;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientTicker implements ITickHandler {
    public static LinkedBlockingQueue<SpawnParticlePacket> spawnQueue = new LinkedBlockingQueue();

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if (!spawnQueue.isEmpty()) {
            DawnOfSteve.proxy.spawnParticle(spawnQueue.poll());
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return Reference.ModId + "." + this.getClass().getSimpleName();
    }
}
