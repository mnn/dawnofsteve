/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.item;

import monnef.dawn.common.Reference;
import monnef.dawn.entity.EntityKit;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKitBag extends ItemDawn {
    private final EntityKit.KitType type;

    public ItemKitBag(int id, EntityKit.KitType type) {
        super(id);
        this.type = type;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            EntityKit kit = new EntityKit(world, player, stack);
            kit.setType(type);
            world.spawnEntityInWorld(kit);
        }

        return stack;
    }

    @Override
    public void registerIcons(IconRegister register) {
        itemIcon = register.registerIcon(Reference.ModName + ":baguni");
    }
}
