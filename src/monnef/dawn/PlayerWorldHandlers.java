package monnef.dawn;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerWorldHandlers {
    @ForgeSubscribe
    public void onItemToss(ItemTossEvent evt) {
        evt.setCanceled(true);
    }

    @ForgeSubscribe
    public void onPlayerDeathDrop(PlayerDropsEvent evt) {
        evt.setCanceled(true);
    }

    @ForgeSubscribe
    public void onBreakSpeed(PlayerEvent.BreakSpeed evt) {
        evt.setCanceled(true);
    }
}
