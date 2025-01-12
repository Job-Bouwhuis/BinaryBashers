package dev.WinterRose.SaxionEngine;

import BinaryBashers.Enemies.EnemyFormat;
import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

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

    private Sound removeLetterSound = new Sound("resources/audio/letterTyping/removeLetter.wav");
    private Sound typeLetterSound = new Sound("resources/audio/letterTyping/typeLetter.wav");
    private Sound inputConfirmSound = new Sound("resources/audio/letterTyping/inputConfirm.wav");
    private ArrayList<Character> cachedAllowedCharacters;
    private boolean allowAllCharacters = false;

    public final ArrayList<Character> binaryCharacters = new ArrayList<>(Arrays.asList('1', '0'));
    public final ArrayList<Character> hexCharacters = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'));
    public final ArrayList<Character> decimalCharacters = new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    public final ArrayList<Character> customAllowedCharacters = new ArrayList<>();
    private ArrayList<EnemyFormat> acceptedFormats = new ArrayList<>();

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
    public void keyPress(KeyEvent key, boolean pressed)
    {
        if (!pressed) return;

        int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER)
        {
            if (inputText.isEmpty()) return;
            onEnterKeyPressed.invoke(this);
            inputConfirmSound.play();
            return;
        }
        if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_NUMPAD9)
        {
            keyCode -= 48;
        }
        if (keyCode >= 65 && keyCode <= 65 + 26)
        {
            if (!onlyCapitalLetters)
            {
                if (!key.isShiftDown())
                {
                    int offset = keyCode - 65;
                    keyCode = 97 + offset;
                }
            }
        }
        if (keyCode == KeyEvent.VK_BACK_SPACE)
        {
            if (inputText.isEmpty()) return;
            inputText.removeLast();
            removeLetterSound.play();
            return;
        }
        int finalKeyCode = keyCode;
        if (Arrays.stream(blacklistedCharacters)
                .anyMatch(blacklistedChar -> blacklistedChar.charValue() == finalKeyCode)) return;

        if (allowAllCharacters)
        {
            addCharacter((char)keyCode);
            return;
        }

        var chars = getUnifiedAllowedCharacters();
        if(!chars.contains((char)finalKeyCode))
            return; // disallowed character

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
        typeLetterSound.play();
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet) {
        typedCharacterColor = colorPallet.getColorFromIndex(6);
        placeholderCharacterColor = colorPallet.getColorFromIndex(5);

    }

    private ArrayList<Character> getUnifiedAllowedCharacters()
    {
        if(cachedAllowedCharacters != null)
            return cachedAllowedCharacters;

        ArrayList<Character> result = new ArrayList<>();
        if(acceptedFormats.contains(EnemyFormat.Decimal))
        {
            result.addAll(decimalCharacters);
        }
        if(acceptedFormats.contains(EnemyFormat.Hex))
        {
            result.addAll(hexCharacters);
        }
        if(acceptedFormats.contains(EnemyFormat.Binary))
        {
            result.addAll(binaryCharacters);
        }
        result.addAll(customAllowedCharacters);
        return cachedAllowedCharacters = result;
    }

    public void allowFormat(EnemyFormat inputFormat)
    {
        if(acceptedFormats.contains(inputFormat))
            return;

        acceptedFormats.add(inputFormat);
        cachedAllowedCharacters = null;
    }

    public void disallowFormat(EnemyFormat format)
    {
        if(acceptedFormats.contains(format))
            acceptedFormats.remove(format);
        cachedAllowedCharacters = null;
    }
}
