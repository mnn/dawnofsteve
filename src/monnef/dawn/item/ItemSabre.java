/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.EnumHelper;

public class ItemSabre extends ItemSword implements IHitWithCoolDown {
    /*    WOOD(0, 59, 2.0F, 0, 15),
        STONE(1, 131, 4.0F, 1, 5),
        IRON(2, 250, 6.0F, 2, 14),
        EMERALD(3, 1561, 8.0F, 3, 10),
        GOLD(0, 32, 12.0F, 0, 22);
            */
    public static EnumToolMaterial enumToolMaterialSabre = EnumHelper.addToolMaterial("sabre", 0, 100, 2f, 2, 10);

    public ItemSabre(int par1, EnumToolMaterial par2EnumToolMaterial) {
        super(par1, par2EnumToolMaterial);
    }

    @Override
    public int getHitCoolDown() {
        return 40;
    }
}
