/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.server;

import cpw.mods.fml.relauncher.ReflectionHelper;
import monnef.dawn.item.ItemGun;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.ServerPlayerBase;
import net.minecraft.util.FoodStats;

import java.lang.reflect.Field;

public class PlayerHooksServer extends ServerPlayerBase {
    private final Field foodTimer;
    private int ticker = 0;

    public PlayerHooksServer(ServerPlayerAPI var1) {
        super(var1);
        this.foodTimer = ReflectionHelper.findField(FoodStats.class, "foodTimer", "field_75123_d");
    }

    @Override
    public void beforeOnUpdate() {
        ticker++;

        FoodStats stats = player.getFoodStats();
        if (stats.getFoodLevel() >= 18 && player.shouldHeal()) {
            if (ticker % 2 == 0) {
                try {
                    Integer oldFoodTimer = (Integer) foodTimer.get(stats);
                    Integer newFoodTimer = oldFoodTimer - 1;
                    foodTimer.set(stats, newFoodTimer);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        super.beforeOnUpdate();
    }
}
