/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.dawn.common.CommonProxy;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.RenderPlayerAPI;

import static monnef.dawn.common.Reference.ModId;

public class ClientProxy extends CommonProxy {
    @Override
    public void onLoad() {
        if (isClient()) {
            KeyBindingRegistry.registerKeyBinding(new DawnKeyHandler());
        }
    }

    @Override
    public void onPreLoad() {
        if (isClient()) {
            PlayerAPI.register(ModId, PlayerHooksClient.class);
            RenderPlayerAPI.register(ModId, PlayerRendering.class);
        }
    }

    @Override
    public int addArmor(String name) {
        return RenderingRegistry.addNewArmourRendererPrefix(name);
    }
}
