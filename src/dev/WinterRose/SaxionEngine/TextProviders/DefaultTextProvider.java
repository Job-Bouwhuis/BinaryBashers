package dev.WinterRose.SaxionEngine.TextProviders;

import dev.WinterRose.SaxionEngine.DrawableCharacter;

import java.awt.*;

public class DefaultTextProvider extends TextProvider
{
    private DrawableCharacter[] characters;

    public DefaultTextProvider() { }

    public DefaultTextProvider(String text)
    {
        setText(text);
    }

    @Override
    public DrawableCharacter[] getCharacters()
    {
        return characters;
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);
        buildText();
    }

    @Override
    public void setColor(Color textColor)
    {
        super.setColor(textColor);
        buildText();
    }

    private void buildText()
    {
        String text = getText();
        characters = new DrawableCharacter[text.length()];
        for (int i = 0; i < text.length(); i++)
        {
            Character c = text.charAt(i);
            characters[i] = new DrawableCharacter(c, defaultColor);
        }
    }
}
