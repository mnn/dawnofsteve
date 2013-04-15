/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import monnef.dawn.entity.EntitySplashBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSplashBomb extends ItemDawn {
    public ItemSplashBomb(int id) {
        super(id);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // TODO cd!

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntitySplashBomb(world, player));
        }

        return stack;
    }

}
