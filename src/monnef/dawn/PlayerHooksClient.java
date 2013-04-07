package monnef.dawn;

import net.minecraft.src.PlayerAPI;
import net.minecraft.src.PlayerBase;

public class PlayerHooksClient extends PlayerBase {
    public PlayerHooksClient(PlayerAPI api) {
        super(api);
    }

    @Override
    public void jump() {
        if (player.isSneaking() && player.capabilities.isCreativeMode) {
            player.motionY = 1;
        } else {
            super.jump();
        }
    }

}
