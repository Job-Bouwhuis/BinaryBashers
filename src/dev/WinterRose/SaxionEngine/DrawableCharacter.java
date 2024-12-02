package dev.WinterRose.SaxionEngine;

import java.awt.*;

public class DrawableCharacter
{
    public Character character;
    public Color color;

    public DrawableCharacter(Character c, Color col)
    {
        this.character = c;
        color = col;
    }

    public DrawableCharacter(Character c)
    {
        character = c;
        color = Color.white;
    }

    public boolean isSpace()
    {
        return character == ' ';
    }

    public boolean isNewline()
    {
        return character == '\n';
    }

    public boolean isReturn()
    {
        return character == '\r';
    }

    public boolean isSpaceNewlineOrReturn()
    {
        return isSpace() || isNewline() || isReturn();
    }
}
