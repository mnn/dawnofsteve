/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import monnef.core.Reference;

import java.util.EnumSet;

public class ClientTicker implements ITickHandler {
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

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
