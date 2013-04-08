/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import monnef.dawn.common.PlayerInfo;
import monnef.dawn.network.NetworkHelper;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;

public class PlayerHooksClient extends PlayerBase {
    PlayerInfo info;
    public static PlayerHooksClient instance;

    public PlayerHooksClient(PlayerAPI api) {
        super(api);
        info = new PlayerInfo(player);
        instance = this;
    }

    @Override
    public void jump() {
        if (player.isSneaking() && player.capabilities.isCreativeMode) {
            player.motionY = 1;
        } else {
            super.jump();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (info.isReloading() && !info.reloadSlotEquals()) {
            info.resetState();
            NetworkHelper.sendToServer(ReloadPacket.ReloadType.CS_STOPPED);
        }
    }

    public void tryReload() {
        if (info.isGunEquipped() && info.gotAmmo() && info.haveSpaceInGun()) {
            NetworkHelper.sendToServer(ReloadPacket.ReloadType.CS_WANT_RELOAD);
        }
    }
}
