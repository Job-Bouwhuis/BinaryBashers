package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;

import java.awt.*;

public class AnimatedSpriteRenderer extends ActiveRenderer
{
    public boolean active = true;
    public Sprite[] sprites;
    private int currentFrame;
    public Vector2 origin;
    public Color tint = Color.white;
    public float secondsPerFrame;
    private double animationProgressTimer;
    private final float animationDuration;
    private boolean isLooping;
    public boolean hideOnEnd;
    private boolean hidden;

    public AnimatedSpriteRenderer(Sprite[] sprites, float secondsPerFrame, boolean isLooping)
    {
        this.sprites = sprites;
        this.secondsPerFrame = secondsPerFrame;
        this.isLooping = isLooping;
        origin = new Vector2(0.5f, 0.5f);
        animationDuration = (sprites.length) * secondsPerFrame;
    }

    public AnimatedSpriteRenderer(Sprite[] sprites, float secondsPerFrame, boolean isLooping, boolean startActive)
    {
        this(sprites, secondsPerFrame, isLooping);
        active = startActive;
    }

    @Override
    public void render(Painter painter)
    {
        if (!hidden && active) {
            painter.drawSprite(sprites[currentFrame], transform, origin, tint);
        }
    }

    public void manualRender(Painter painter, Transform transform)
    {
        if (!hidden && active) {
            painter.drawSprite(sprites[currentFrame], transform, origin, tint);
        }
    }

    @Override
    public void update() {
        if (!active)
            return;
        if (animationProgressTimer < animationDuration) {
            hidden = false;
            animationProgressTimer += 1 * Time.getDeltaTime();
        }
        else if (isLooping) {
            animationProgressTimer = 0;
        }
        else if (hideOnEnd) {
            hidden = true;
        }
        currentFrame = Math.min((int)Math.floor(animationProgressTimer / secondsPerFrame), sprites.length - 1);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = SpritePalletChanger.changePallet(sprites[i], colorPallet);
        }
    }

    public boolean isHidden()
    {
        return hidden;
    }
}
