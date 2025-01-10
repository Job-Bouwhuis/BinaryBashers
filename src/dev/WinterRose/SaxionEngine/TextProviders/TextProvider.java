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

    public void setDefaultColor(Color color)
    {
        defaultColor = color;
    }
    public Color getDefaultColor()
    {
        return defaultColor;
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
        if(words.isEmpty())
            return FontType.Normal;

        return words.get(0).fontType;
    }

    public void setFontType(FontType fontType)
    {
        for(Word wrd : words)
            wrd.fontType = fontType;
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
        textValue = text;
        defaultColor = color;
        for (Word wrd : words)
            wrd.fontType = fontType;
        buildWordsList();
    }

    public void setWords(ArrayList<Word> words)
    {
        this.words = words;
    }
}


