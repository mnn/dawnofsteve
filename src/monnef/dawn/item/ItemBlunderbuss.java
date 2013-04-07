package monnef.dawn.item;

import monnef.dawn.DawnOfSteve;

public class ItemBlunderbuss extends ItemGun {
    public ItemBlunderbuss(int id) {
        super(id);
        setUnlocalizedName(DawnOfSteve.BLUNDERBUSS);
        setFull3D();
        initGun(AmmoRequirement.BULLETS_SMALL, 1, 20, 5);
    }
}
