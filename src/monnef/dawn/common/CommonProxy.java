package monnef.dawn.common;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
    public boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }
}