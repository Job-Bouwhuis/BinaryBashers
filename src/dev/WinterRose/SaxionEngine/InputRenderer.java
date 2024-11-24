package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;
import nl.saxion.app.interaction.KeyboardEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class InputRenderer extends Renderer implements IKeystrokeCallback
{
    public Character placeholderChar = '_';
    public String targetText;
    public ArrayList<DrawableCharacter> inputText = new ArrayList<>();
    public Vector2 origin = new Vector2(0.5f, 0.5f);
    public Character[] acceptedCharacters;
    public Consumer<InputRenderer> onCorrectTextComplete;

    public Color correctCharacterColor = Color.cyan;
    public Color wrongCharacterColor = Color.red;
    public Color placeholderCharacterColor = Color.white;

    public InputRenderer(String targetText)
    {
        this.targetText = targetText;
    }

    @Override
    public void render(Painter painter)
    {
        DrawableCharacter[] chars = new DrawableCharacter[targetText.length()];
        for(int i = 0; i < chars.length; i++)
        {
            if(inputText.size() <= i)
                chars[i] = new DrawableCharacter(placeholderChar, placeholderCharacterColor);
            else
                chars[i] = inputText.get(i);
        }

        painter.drawText(chars, transform, origin);
    }

    @Override
    public void keyPress(KeyboardEvent key)
    {
        if (!key.isKeyPressed()) return;

        try
        {
            int keyCode = key.getKeyCode();
            if (keyCode == KeyboardEvent.VK_BACK_SPACE)
            {
                if (inputText.isEmpty()) return;
                inputText.removeLast();
                return;
            }

            if (acceptedCharacters == null)
            {
                addCharacter((char)keyCode);
                return;
            }

            if (!Arrays.stream(acceptedCharacters).anyMatch(c -> ((int) c.charValue()) == keyCode)) return;

            addCharacter((char)keyCode);
        }
        finally
        {
            if (buildStringFromInputText().equals(targetText) && onCorrectTextComplete != null) onCorrectTextComplete.accept(this);
        }
    }

    private String buildStringFromInputText()
    {
        StringBuilder sb = new StringBuilder();

        for(var c : inputText)
            sb.append(c.character);

        return sb.toString();
    }

    private void addCharacter(Character c)
    {
        if(inputText.size() == targetText.length())
            return; // already max size.

        boolean valid = targetText.charAt(inputText.size()) == c;
        Color col;
        if(valid)
            col = correctCharacterColor;
        else
            col = wrongCharacterColor;
        inputText.add(new DrawableCharacter(c, col));
    }
}
