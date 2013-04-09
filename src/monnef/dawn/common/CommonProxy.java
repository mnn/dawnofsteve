/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.common;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
    public boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public boolean isClient() {
        return !isServer();
    }

    public void onLoad() {
    }

    public void onPreLoad() {
    }

    public int addArmor(String name) {
        return 0;
    }
}
