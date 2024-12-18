package dev.WinterRose.SaxionEngine.TextProviders;

import dev.WinterRose.SaxionEngine.Sound;
import dev.WinterRose.SaxionEngine.SoundPack;

import java.util.ArrayList;

public class AnimatedTextProvider extends TextProvider
{
    private final int nextCharacterCalls;
    private String text;
    private int calls;
    private int currentCharacterIndex = 0;

    private boolean playSound = true;
    private SoundPack sounds;
    private int soundEveryXCharacters = 2;

    /**
     * Creates a new AnimatedTextProvider instance
     * @param text the text to be renderered
     * @param framesPerCharacter How many calls it takes per character to be added to the total text
     */
    public AnimatedTextProvider(String text, int framesPerCharacter)
    {
        this.text = text;
        calls = nextCharacterCalls = framesPerCharacter;
        forceSetText("");
        sounds = new SoundPack("resources/audio/letterTyping");
        sounds.setAllVolume(.8f);
    }

    @Override
    public ArrayList<Word> getWords()
    {
        if (calls >= nextCharacterCalls)
        {
            addCharacter();
            calls = 0;
        }

        calls++;
        return words;
    }

    private void addCharacter()
    {
        if (currentCharacterIndex == text.length()) return; // no more characters to add.

        if (currentCharacterIndex % soundEveryXCharacters == 0 && playSound)
            sounds.playRandom();

        String textOnScreen = getText();
        forceSetText(textOnScreen + text.charAt(currentCharacterIndex));

        currentCharacterIndex++;
    }

    @Override
    public void setText(String text)
    {
    }

    private void forceSetText(String text)
    {
        super.setText(text);
    }

    public float getAnimationPercent()
    {
        float onScreen = getText().length();
        float target = text.length();

        return onScreen / target;
    }

    public void setPlaySounds(boolean playSounds)
    {
        playSound = playSounds;
    }
}
