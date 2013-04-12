/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemArmorDawn extends ItemArmor implements ISpecialArmorModel, IArmorTextureProvider {
        /*
CLOTH(5, new int[]{1, 3, 2, 1}, 15),
CHAIN(15, new int[]{2, 5, 4, 1}, 12),
IRON(15, new int[]{2, 6, 5, 2}, 9),
GOLD(7, new int[]{2, 5, 3, 1}, 25),
DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
*/

    public static EnumArmorMaterial EnumArmorMaterialDawn01 = EnumHelper.addArmorMaterial("01", 5, new int[]{1, 3, 2, 1}, 20);

    private final String armorTexture;
    private ArmorModelEnum model;

    public enum ArmorType {
        helm, chest, leggings, boots
    }

    public ItemArmorDawn(int id, EnumArmorMaterial material, int renderIndex, ArmorType armorType, String armorTexture, ArmorModelEnum model) {
        super(id, material, renderIndex, armorType.ordinal());
        this.armorTexture = armorTexture;
        this.model = model;
    }

    @Override
    public ArmorModelEnum getArmorModel() {
        return model;
    }

    @Override
    public String getArmorTextureFile(ItemStack itemstack) {
        return armorTexture;
    }
}
