/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.common;

import net.minecraft.block.Block;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerWorldHandlers {
    @ForgeSubscribe
    public void onItemToss(ItemTossEvent evt) {
        if (!evt.player.capabilities.isCreativeMode) {
            evt.setCanceled(true);
        }
    }

    @ForgeSubscribe
    public void onPlayerDeathDrop(PlayerDropsEvent evt) {
        evt.setCanceled(true);
    }

    @ForgeSubscribe
    public void onBreakSpeed(PlayerEvent.BreakSpeed evt) {
        if (evt.block.blockID == Block.cloth.blockID) {

        } else {
            evt.setCanceled(true);
        }
    }
}
