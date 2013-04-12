/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import monnef.core.MonnefCorePlugin;
import monnef.dawn.DawnOfSteve;

public class ItemBlunderbuss extends ItemGun {
    public ItemBlunderbuss(int id) {
        super(id);
        setUnlocalizedName(DawnOfSteve.BLUNDERBUSS);
        initBasic(AmmoRequirement.BULLETS_SMALL, MonnefCorePlugin.debugEnv ? 20 : 1, 20, 5);
    }
}
