package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;
import nl.saxion.app.interaction.KeyboardEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class InputRenderer extends Renderer implements IKeystrokeCallback
{
    public int characterMax;
    public Character placeholderChar = '_';
    public ArrayList<DrawableCharacter> inputText = new ArrayList<>();
    public Vector2 origin = new Vector2(0.5f, 0.5f);
    public Action<InputRenderer> onEnterKeyPressed = new Action<>();
    public Character[] acceptedCharacters;
    public boolean onlyCapitalLetters = true;

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

    public Color typedCharacterColor = Color.cyan;
    public Color placeholderCharacterColor = Color.white;

    public InputRenderer(int characterMax)
    {
        this.characterMax = characterMax;
    }

    @Override
    public void render(Painter painter)
    {
        DrawableCharacter[] chars = new DrawableCharacter[characterMax];
        for (int i = 0; i < chars.length; i++)
        {
            if (inputText.size() <= i) chars[i] = new DrawableCharacter(placeholderChar, placeholderCharacterColor);
            else chars[i] = inputText.get(i);
        }

        painter.drawText(chars, transform, origin, fontType);
    }

    @Override
    public void keyPress(KeyboardEvent key)
    {
        if (!key.isKeyPressed()) return;

        int keyCode = key.getKeyCode();
        if (keyCode == KeyboardEvent.VK_ENTER)
        {
            onEnterKeyPressed.invoke(this);
            return;
        }
        if(keyCode >= KeyboardEvent.VK_NUMPAD0 && keyCode <= KeyboardEvent.VK_NUMPAD9)
        {
            keyCode -= 48;
        }
        if(keyCode >= 65 && keyCode <= 65 + 26)
        {
            if(!onlyCapitalLetters)
            {
                if(!key.isShiftDown())
                {
                    int offset = keyCode - 65;
                    keyCode = 97 + offset;
                }
            }
        }
        if (keyCode == KeyboardEvent.VK_BACK_SPACE)
        {
            if (inputText.isEmpty()) return;
            inputText.removeLast();
            return;
        }
        int finalKeyCode = keyCode;
        if (Arrays.stream(blacklistedCharacters).anyMatch(blacklistedChar -> blacklistedChar.charValue() == finalKeyCode))
            return;

        if (acceptedCharacters == null)
        {
            addCharacter((char) keyCode);
            return;
        }

        if (!Arrays.stream(acceptedCharacters).anyMatch(c -> ((int) c.charValue()) == finalKeyCode)) return;

        addCharacter((char) keyCode);
    }

    public String getInputAsString()
    {
        return EngineUtil.getStringFromDrawableCharacters(inputText);
    }

    private void addCharacter(Character c)
    {
        if (inputText.size() == characterMax) return; // already max size.
        inputText.add(new DrawableCharacter(c, typedCharacterColor));
    }
}
