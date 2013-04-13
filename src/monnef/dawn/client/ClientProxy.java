/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import monnef.dawn.common.CommonProxy;
import monnef.dawn.entity.EntityKit;
import monnef.dawn.network.packet.SpawnParticlePacket;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.PlayerAPI;
import net.minecraft.src.RenderPlayerAPI;
import net.minecraft.world.World;

import static monnef.dawn.common.Reference.ModId;

public class ClientProxy extends CommonProxy {
    @Override
    public void onLoad() {
        if (isClient()) {
            KeyBindingRegistry.registerKeyBinding(new DawnKeyHandler());
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityKit.class, new EntityKitRenderer());
    }

    @Override
    public void onPreLoad() {
        if (isClient()) {
            PlayerAPI.register(ModId, PlayerHooksClient.class);
            RenderPlayerAPI.register(ModId, PlayerRendering.class);
        }
    }

    @Override
    public int addArmor(String name) {
        return RenderingRegistry.addNewArmourRendererPrefix(name);
    }

    private void spawnParticle(EntityFX entity) {
        if (entity != null) {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(entity);
        }
    }

    @Override
    public void spawnParticle(SpawnParticlePacket packet, EntityPlayer player) {
        World world = player.worldObj;

        switch (packet.getType()) {
            case BULLET_SMOKE:
                spawnParticle(new EntitySmokeFX(world, packet.getX(), packet.getY(), packet.getZ(), 0, 0, 0, 1.5f));
                break;

            case GUNPOWDER_SMOKE:
                spawnParticle(new EntitySmokeFX(world, packet.getX(), packet.getY(), packet.getZ(), 0, 0.001, 0, 2f));
                break;
        }
    }
}
