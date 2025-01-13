package dev.WinterRose.SaxionEngine.ColorPallets;

import dev.WinterRose.SaxionEngine.Sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpritePalletChanger
{
    private static final Color fallbackMapColor = new Color(255, 0, 255, 255);
    private static final Color transparentColor = new Color(0, 0, 0, 0);

    /**
     * Changes the colors of the given sprite according to the given ColorPallet, places a full Magenta colored pixel in the scenario of a unknown color mapping for any given color, and prints this
     * in the standard System.out
     * @param sprite
     * @param pallet
     * @return
     */
    public static Sprite changePallet(Sprite sprite, ColorPallet pallet)
    {
        Color[] spriteColors = sprite.getColorData();

        int width = sprite.getWidth();

        BufferedImage result = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0, spriteColorsLength = spriteColors.length; i < spriteColorsLength; i++)
        {
            Color map = pallet.get(spriteColors[i], fallbackMapColor);
            if (spriteColors[i].equals(transparentColor) && map.equals(fallbackMapColor))
                map = transparentColor;

            int x = i % width;
            int y = i / width;

            if(map.equals(fallbackMapColor))
                System.out.printf("\u001B[31mColor %s at pixel coördinate (%s, %s) was not mapped. Placing fallback color!\u001B[0m%n", spriteColors[i].toString(), x, y);

            result.setRGB(x, y, map.getRGB());
        }

        return new Sprite(result);
    }
}
