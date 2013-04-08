/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import monnef.dawn.DawnOfSteve;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class DawnCreativeTab extends CreativeTabs {
    public DawnCreativeTab(String modId) {
        super(modId);
    }

    @Override
    public Item getTabIconItem() {
        return DawnOfSteve.blunderbuss;
    }
}
