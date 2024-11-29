package dev.WinterRose.SaxionEngine;

public class EngineUtil
{
    public static String getStringFromDrawableCharacters(DrawableCharacter[] characters)
    {
        StringBuilder sb = new StringBuilder();

        for(var c : characters)
            sb.append(c.character);

        return sb.toString();
    }
}
