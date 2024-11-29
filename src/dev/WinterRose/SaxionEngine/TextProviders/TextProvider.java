package dev.WinterRose.SaxionEngine.TextProviders;

import dev.WinterRose.SaxionEngine.DrawableCharacter;
import dev.WinterRose.SaxionEngine.FontType;

import java.awt.*;

public abstract class TextProvider
{
    protected String textValue = "New Text";
    protected Color defaultColor = Color.white;
    protected FontType fontType = FontType.Normal;

    public abstract DrawableCharacter[] getCharacters();

    public String getText()
    {
        return textValue;
    }

    public void setText(String text)
    {
        textValue = text;
    }

    public Color getColor()
    {
        return defaultColor;
    }

    public void setColor(Color textColor)
    {
        defaultColor = textColor;
    }

    public FontType getFontType()
    {
        return fontType;
    }

    public void setFontType(FontType fontType)
    {
        this.fontType = fontType;
    }
}
