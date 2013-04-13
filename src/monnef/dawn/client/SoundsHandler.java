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

public class SoundsHandler {
    public static final String CLICK = "dawn_click.wav";
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

    private void loadSound(String fileName) {
        String path = "/mods/" + Reference.ModName + "/sounds/" + fileName;
        URL resource = DawnOfSteve.class.getResource(path);
        if (resource == null) throw new RuntimeException("Not found: \"" + path + "\".");
        manager.soundPoolSounds.addSound(fileName, resource);
    }

    public static void playSoundAtEntity(Entity entity, String id, int volume, int pitch) {
        id = id.substring(0, id.indexOf("."));
        entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, id, volume, pitch, false);
    }
}
