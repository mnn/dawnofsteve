/*
 * Copyright (c) 2013 monnef.
 */

package monnef.dawn.client;

public enum SoundsEnum {
    CLICK("dawn_click.wav");

    public static final boolean randomSoundsEnabled = true;
    private String fileName;
    private String playName;

    SoundsEnum(String fileName) {
        this.fileName = fileName;
        this.playName = fileName.substring(0, fileName.indexOf("."));
        if (randomSoundsEnabled)
            while (Character.isDigit(playName.charAt(playName.length() - 1)))
                playName = playName.substring(0, playName.length() - 1);
        playName = playName.replaceAll("/", ".");
    }

    public String getPlayName() {
        return playName;
    }

    public String getFileName() {
        return fileName;
    }
}
