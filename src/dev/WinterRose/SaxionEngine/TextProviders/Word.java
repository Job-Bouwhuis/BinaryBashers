package dev.WinterRose.SaxionEngine.TextProviders;


import dev.WinterRose.SaxionEngine.DrawableCharacter;
import dev.WinterRose.SaxionEngine.FontType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a word that can be drawn to the screen with unique font, color, and font type
 */
public class Word
{
    /**
     * The font to use for this specific word. keep NULL to use Painter.Font by default
     */
    public Font font;
    public FontType fontType = FontType.Normal;

    DrawableCharacter[] characters;
    public Character paddingChar = null; // the space, newline, return, etc character if applicable

    public Word(Stream<Character> text, TextProvider owner)
    {
        ArrayList<DrawableCharacter> chars = new ArrayList<>();

        DrawableCharacter c = new DrawableCharacter(text.takeNext(), owner.defaultColor);
        while(c.character != null && !c.isSpaceNewlineOrReturn())
        {
            chars.add(c);
            c = new DrawableCharacter(text.takeNext(), owner.defaultColor);
        }

        if (c.character != null && c.isSpaceNewlineOrReturn()) // end of stream
        {
            paddingChar = c.character;
            owner.continueReadingText(text);
        }

        // copy letters from chars to the characters array so they are stored.
        characters = new DrawableCharacter[chars.size()];
        for (int i = 0; i < chars.size(); i++)
        {
            var ch = chars.get(i);
            characters[i] = ch;
        }
    }

    public Word(String word, TextProvider owner)
    {
        this(Stream.getCharacterStream(word), owner);
    }

    public static ArrayList<Word> ParseWords(String s, TextProvider owner)
    {
        ArrayList<Word> result = new ArrayList<>();
        String[] split = s.split(" ");
        for (int i = 0; i < split.length; i++)
        {
            String section = split[i];
            if(i + 1 < split.length)
                section += " ";
            result.add(new Word(section, owner));
        }
        return result;
    }

    public DrawableCharacter[] getCharacters()
    {
        return characters;
    }

    /**
     * Sets the color of all characters in this word to the given color
     * @param color the color to give to the characters in the word
     */
    public void setColor(Color color)
    {
        for(var c : characters)
            c.color = color;
    }
}