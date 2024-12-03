package dev.WinterRose.SaxionEngine.TextProviders;

import dev.WinterRose.SaxionEngine.DrawableCharacter;
import dev.WinterRose.SaxionEngine.FontType;

import java.awt.*;
import java.util.ArrayList;

public abstract class TextProvider
{
    protected String textValue = "New Text";
    protected Color defaultColor = Color.white;
    protected FontType fontType = FontType.Normal;
    protected int characterXPadding = 1;
    protected int characterYPadding = 2;

    protected ArrayList<Word> words = new ArrayList<>();

    public ArrayList<Word> getWords()
    {
        return words;
    }

    public int getCharacterXPadding()
    {
        return characterXPadding;
    }

    public int getCharacterYPadding()
    {
        return characterYPadding;
    }

    public String getText()
    {
        return textValue;
    }

    public void setText(String text)
    {
        textValue = text;
        buildWordsList();
    }

    public Color getColor()
    {
        return defaultColor;
    }

    public void setColor(Color textColor)
    {
        defaultColor = textColor;
        buildWordsList();
    }

    public FontType getFontType()
    {
        return fontType;
    }

    public void setFontType(FontType fontType)
    {
        this.fontType = fontType;
        buildWordsList();
    }

    protected void buildWordsList()
    {
        words.clear();
        Font prevFont = null;
        if(!words.isEmpty())
        {
            prevFont = words.getFirst().font;
        }
        var text = Stream.getCharacterStream(textValue);
        continueReadingText(text);

        ArrayList<Word> result = new ArrayList<>();
        int size = words.size();
        for (int i = size - 1; i >= 0; i--)
            result.add(words.get(i));

        words = result;
        if(prevFont != null)
            setFont(prevFont);
    }

    /**
     * This method was made protected so it can be overidden, do not call manually unless you know what youre doing. Favor calling 'buildWordsList()' instead
     * @param text
     */
    protected void continueReadingText(Stream<Character> text)
    {
        Word word = new Word(text, this);
        words.add(word);
    }

    public void setFont(Font font)
    {
        for(Word wrd : words)
            wrd.font = font;
    }

    public void setTextColorAndFontType(String text, Color color, FontType fontType)
    {
        setText(text);
        setColor(color);
        setFontType(fontType);
        buildWordsList();
    }

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
}


