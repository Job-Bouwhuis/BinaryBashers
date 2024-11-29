package dev.WinterRose.SaxionEngine;

import java.util.ArrayList;

public class EngineUtil
{
    public static String getStringFromDrawableCharacters(DrawableCharacter[] characters)
    {
        StringBuilder sb = new StringBuilder();

        for(var c : characters)
            sb.append(c.character);

        return sb.toString();
    }

    public static String getStringFromDrawableCharacters(ArrayList<DrawableCharacter> characters)
    {
        StringBuilder sb = new StringBuilder();

        for(var c : characters)
            sb.append(c.character);

        return sb.toString();
    }
}
