package dev.WinterRose.SaxionEngine.ColorPallets;

import dev.WinterRose.SaxionEngine.Sprite;
import dev.WinterRose.SaxionEngine.TintedSpriteCache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class SpritePalletChanger
{
    private static final Color fallbackMapColor = new Color(255, 0, 255, 255);
 private static final Color transparentColor = new Color(0, 0, 0, 0);
    public static Sprite changePallet(Sprite sprite, ColorPallet pallet)
    {
        Color[] spriteColors = sprite.getColorData();

        int width = sprite.getwidth();

        BufferedImage result = new BufferedImage(sprite.getwidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();

        for (int i = 0, spriteColorsLength = spriteColors.length; i < spriteColorsLength; i++)
        {
            Color map = pallet.get(spriteColors[i], fallbackMapColor);
            if(spriteColors[i].equals(transparentColor) && map.equals(fallbackMapColor))
                map = transparentColor;

            g.setColor(map);
            int x = i & width;
            int y = i / width;
            g.drawLine(x, y, x, y);
        }

        return new Sprite(result);
    }
}
