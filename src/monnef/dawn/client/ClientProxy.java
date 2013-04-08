/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import monnef.dawn.common.CommonProxy;

public class ClientProxy extends CommonProxy {
    @Override
    public void onLoad() {
        KeyBindingRegistry.registerKeyBinding(new DawnKeyHandler());
    }
}
