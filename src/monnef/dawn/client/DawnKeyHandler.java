/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import monnef.dawn.network.NetworkHelper;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;

import java.util.EnumSet;

public class DawnKeyHandler extends KeyBindingRegistry.KeyHandler {

    public static final String RELOAD = "Reload";

    public DawnKeyHandler() {
        super(new KeyBinding[]{new KeyBinding(RELOAD, 19)}, new boolean[]{false});
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        if (kb.keyDescription.equals(RELOAD)) {
            EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
            player.addChatMessage("reload key pressed!");
            PlayerHooksClient.instance.tryReload();
        }
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return this.getClass().getSimpleName();
    }
}
