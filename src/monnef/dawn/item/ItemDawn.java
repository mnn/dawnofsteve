/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import monnef.dawn.DawnOfSteve;
import monnef.dawn.common.Reference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

public class ItemDawn extends Item {
    protected int hitCollDown = 0;
    private int meleeDamage = 1;

    public ItemDawn(int id) {
        super(id);
        setCreativeTab(DawnOfSteve.CreativeTab);
    }

    @Override
    public void registerIcons(IconRegister register) {
        this.itemIcon = register.registerIcon(getIconName(this));
    }

    public static String getIconName(Item item) {
        String id = item.getUnlocalizedName();
        id = id.substring(id.indexOf('.') + 1);
        return Reference.ModName + ":" + id;
    }

    @Override
    public int getDamageVsEntity(Entity par1Entity) {
        return meleeDamage;
    }

    public ItemDawn setMeleeDamage(int meleeDamage) {
        this.meleeDamage = meleeDamage;
        return this;
    }

    public ItemDawn setHitCollDown(int newCoolDown) {
        hitCollDown = newCoolDown;
        return this;
    }
}
