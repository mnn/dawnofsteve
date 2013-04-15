/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

import monnef.dawn.DawnOfSteve;
import monnef.dawn.common.Reference;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

import java.net.URL;

import static monnef.dawn.client.SoundsEnum.CLICK;

public class SoundsHandler {
    private static SoundManager manager;

    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent evt) {
        try {
            manager = evt.manager;
            loadSound(CLICK);
        } catch (Exception e) {
            DawnOfSteve.Log.printSevere("Failed to register one or more sounds.");
            throw new RuntimeException(e);
        }
    }

    private void loadSound(SoundsEnum sound) {
        String path = "/mods/" + Reference.ModName + "/sounds/" + sound.getFileName();
        URL resource = DawnOfSteve.class.getResource(path);
        if (resource == null) throw new RuntimeException("Not found: \"" + path + "\".");
        manager.soundPoolSounds.addSound(sound.getFileName(), resource);
    }

    public static void playSoundAtEntity(Entity entity, SoundsEnum sound, int volume, int pitch) {
        entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, sound.getPlayName(), volume, pitch, false);
    }
}
