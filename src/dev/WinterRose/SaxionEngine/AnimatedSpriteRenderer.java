package dev.WinterRose.SaxionEngine;

import java.awt.*;

public class AnimatedSpriteRenderer extends ActiveRenderer
{
    public Sprite[] sprites;
    private int currentFrame;
    public Vector2 origin;
    public Color tint = Color.white;
    public float framesPerSecond;
    private double animationProgressTimer;
    private final float animationDuration;

    public AnimatedSpriteRenderer(Sprite[] sprites, float framesPerSecond)
    {
        this.sprites = sprites;
        this.framesPerSecond = framesPerSecond;
        origin = new Vector2(0.5f, 0.5f);
        animationDuration = (sprites.length) * framesPerSecond;
    }

    @Override
    public void render(Painter painter)
    {
        painter.drawSprite(sprites[currentFrame], transform, origin, tint);
    }

    @Override
    public void update() {
        animationProgressTimer += 1 * Time.deltaTime;

        if (animationProgressTimer >= animationDuration) {
            animationProgressTimer = 0;
        }
        currentFrame = (int)Math.floor(animationProgressTimer / framesPerSecond);
        System.out.println(animationProgressTimer);
    }
}
