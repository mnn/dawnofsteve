/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.relauncher.ReflectionHelper;
import monnef.dawn.common.PlayerInfo;
import monnef.dawn.item.IHitWithCoolDown;
import monnef.dawn.network.NetworkHelper;
import monnef.dawn.network.packet.ReloadPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;

import java.lang.reflect.Field;

public class PlayerHooksClient extends PlayerBase {
    PlayerInfo info;
    public static PlayerHooksClient instance;
    private final Field leftClickCounterField;
    private final Field objMouseOver;
    private int swingCd = 0;

    public PlayerHooksClient(PlayerAPI api) {
        super(api);
        info = new PlayerInfo(player);
        instance = this;
        leftClickCounterField = ReflectionHelper.findField(Minecraft.class, "leftClickCounter", "field_71429_W");
        objMouseOver = ReflectionHelper.findField(Minecraft.class, "objectMouseOver", "field_71476_x");
        player.setSpeedInAirField(0.01f);
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

        if (swingCd > 0) swingCd--;
    }


    public void tryReload() {
        if (info.isGunEquipped() && info.gotAmmo() && info.haveSpaceInGun()) {
            NetworkHelper.sendToServer(ReloadPacket.ReloadType.CS_WANT_RELOAD);
        }
    }

    @Override
    public void swingItem() {
        if (swingCd <= 0) {
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof IHitWithCoolDown) {
                    swingCd = ((IHitWithCoolDown) item).getHitCoolDown();
                    setClickCounter(swingCd);
                }
            }
            super.swingItem();
        } else {
            resetObjectOverMouse();
            setClickCounter(100);
        }
    }

    private void setClickCounter(int cd) {
        try {
            leftClickCounterField.setInt(Minecraft.getMinecraft(), cd);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetObjectOverMouse() {
        try {
            objMouseOver.set(Minecraft.getMinecraft(), null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public float getSpeedModifier() {
        if (player.isSprinting()) {
            return 0.87f; // -> 13% faster when sprinting
        }
        return super.getSpeedModifier();
    }

    @Override
    public boolean isSprinting() {
        if (player.capabilities.isCreativeMode) {
            player.setSprinting(false);
        }
        return super.isSprinting();
    }
}
