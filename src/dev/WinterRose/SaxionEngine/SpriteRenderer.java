package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;

import java.awt.*;

public class SpriteRenderer extends Renderer
{
    public Sprite sprite;
    public Vector2 origin;
    public Color tint = Color.white;

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
        origin = new Vector2(0.5f, 0.5f);
    }

    @Override
    public void render(Painter painter)
    {
        painter.drawSprite(sprite, transform, origin, tint);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet) {
        sprite = SpritePalletChanger.changePallet(sprite, colorPallet);
    };
}
