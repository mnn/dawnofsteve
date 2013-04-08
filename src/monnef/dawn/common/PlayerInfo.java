/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.common;

import monnef.dawn.item.IItemGun;
import monnef.dawn.item.ItemGun;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static monnef.dawn.common.PlayerStateEnum.IDLE;
import static monnef.dawn.common.PlayerStateEnum.RELOADING;

public class PlayerInfo {
    PlayerStateEnum state = IDLE;
    int reloadingSlotNumber;
    private EntityPlayer player;

    public PlayerInfo(EntityPlayer player) {
        this.player = player;
    }

    public boolean isReloading() {
        return state == RELOADING;
    }

    public boolean reloadSlotEquals() {
        return player.inventory.currentItem == reloadingSlotNumber;
    }

    public void resetState() {
        state = IDLE;
    }

    public boolean isGunEquipped() {
        return getEquippedGun() != null;
    }

    public ItemStack getEquippedGun() {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack == null) return null;
        Item item = stack.getItem();
        if (!(item instanceof IItemGun)) return null;
        return stack;
    }

    public boolean gotAmmo() {
        ItemStack gun = getEquippedGun();
        if (gun == null) return false;
        // TODO ammo registry?
        return true;
    }

    public boolean haveSpaceInGun() {
        ItemStack stack = getEquippedGun();
        if (stack == null) return false;
        ItemGun gun = (ItemGun) stack.getItem();
        return gun.getAmmoLeft(stack) < gun.getClipSize();
    }
}
