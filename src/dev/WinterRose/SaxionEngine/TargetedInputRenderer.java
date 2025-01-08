package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class TargetedInputRenderer extends Renderer implements IKeystrokeCallback
{
    public Character placeholderChar = '_';
    public String targetText;
    public ArrayList<DrawableCharacter> inputText = new ArrayList<>();
    public Vector2 origin = new Vector2(0.5f, 0.5f);
    public Character[] acceptedCharacters;
    public Action<TargetedInputRenderer> onCorrectTextComplete = new Action<>();
    /**
     * A predefined collection of characters that cant be typed in as a character
     */
    public Character[] blacklistedCharacters = new Character[]{
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_ALT,
            KeyEvent.VK_META,
            KeyEvent.VK_CAPS_LOCK,
            KeyEvent.VK_ESCAPE,
            KeyEvent.VK_ENTER,
            KeyEvent.VK_BACK_SPACE,
            KeyEvent.VK_TAB,
            KeyEvent.VK_DELETE,
            KeyEvent.VK_HOME,
            KeyEvent.VK_END,
            KeyEvent.VK_PAGE_UP,
            KeyEvent.VK_PAGE_DOWN,
            KeyEvent.VK_INSERT,
            KeyEvent.VK_NUM_LOCK,
            KeyEvent.VK_SCROLL_LOCK };
    public FontType fontType = FontType.Normal;

    public Color correctCharacterColor = Color.cyan;
    public Color wrongCharacterColor = Color.red;
    public Color placeholderCharacterColor = Color.white;

    public TargetedInputRenderer(String targetText)
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

        painter.drawText(chars, transform, origin, fontType);
    }

    @Override
    public void keyPress(KeyEvent key, boolean pressed)
    {
        if(!pressed)
            return;

        try
        {
            int keyCode = key.getKeyCode();
            if (keyCode == KeyEvent.VK_BACK_SPACE)
            {
                if (inputText.isEmpty()) return;
                inputText.removeLast();
                return;
            }
            if(Arrays.stream(blacklistedCharacters).anyMatch(c -> c.charValue() == keyCode))
                return;

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
            if (buildStringFromInputText().equals(targetText) && onCorrectTextComplete != null) onCorrectTextComplete.invoke(this);
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
