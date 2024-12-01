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
}
