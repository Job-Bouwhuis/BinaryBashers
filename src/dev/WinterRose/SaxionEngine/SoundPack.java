package dev.WinterRose.SaxionEngine;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;

public class SoundPack
{
    ArrayList<Sound> sounds = new ArrayList<>();

    public SoundPack(String directoryPath)
    {
        File directory = new File(directoryPath);
        for (var file : directory.listFiles())
        {
            if(!file.getName().endsWith(".wav"))
                System.out.println("\u001B[31m Path '%s' is not a audio file, but the directory is given as a source for audio files.\u001B[0m%n");

            sounds.add(new Sound(file.getPath()));
        }
    }

    public void playRandom()
    {
        SecureRandom secureRandom = new SecureRandom();
        int randomValue = secureRandom.nextInt(sounds.size());
        Sound sound = sounds.get(randomValue);
        stopAll();
        sound.play();
    }

    public void setAllVolume(float volume)
    {
        for (Sound sound : sounds)
            sound.setVolume(volume);
    }

    public void stopAll()
    {
        for (Sound sound : sounds)
            sound.stop();
    }
}
