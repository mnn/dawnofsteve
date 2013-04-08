/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import net.minecraft.item.ItemStack;

public interface IItemGun {
    ItemGun.AmmoRequirement getRequiresAmmo();

    int getClipSize();

    float getMaxDistance();

    void initNBT(ItemStack stack);

    int getAmmoLeft(ItemStack stack);

    void setAmmoLeft(ItemStack stack, int count);

    int getCoolDown(ItemStack stack);

    void setCoolDown(ItemStack stack, int count);

    boolean coolDownActive(ItemStack stack);
}
