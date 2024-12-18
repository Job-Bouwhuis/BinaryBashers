package dev.WinterRose.SaxionEngine;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BoxRenderer extends ActiveRenderer
{
    private Rectangle.Float bounds;
    private Rectangle.Float targetBounds;

    public Color boxColor = Color.white;
    public Color backgroundColor = new Color(66, 66, 66, 66);

    public float widthAnimationSpeed = 6f;
    public float heightAnimationSpeed = 6f;

    public float animateOutWidthStartPercent = .6f;
    public float animateInHeightStartPercent = .6f;

    private float animationProgress = 0;

    private boolean animatingIn = false;

    public BoxRenderer(Vector2 size)
    {
        bounds = new Rectangle2D.Float();
        targetBounds = new Rectangle2D.Float();
        targetBounds.setRect(0, 0, size.x, size.y);
    }

    public void showImmediately()
    {
        animatingIn = true;
        bounds.setRect(targetBounds.x, targetBounds.y, targetBounds.width, targetBounds.height);
    }

    public float getAnimationProgress()
    {
        return animationProgress;
    }

    public void hideImmediately()
    {
        animatingIn = false;
        bounds.setRect(targetBounds.x, targetBounds.y, -10, -10);
    }

    public boolean isAnimatingIn()
    {
        return animatingIn;
    }

    @Override
    public void awake()
    {
        var pos = transform.getPosition();
        var scale = transform.getScale();

        float targetWidth = targetBounds.width * scale.x;
        float targetHeight = targetBounds.height * scale.y;

        targetBounds.setRect(
                pos.x - targetWidth / 2,
                pos.y - targetHeight / 2,
                targetWidth,
                targetHeight);

        bounds.x = targetBounds.x;
        bounds.y = targetBounds.y;
    }

    @Override
    public void update()
    {
        awake(); // keep updating the bounds so that they match

        float time = Time.getDeltaTime();

        animationProgress = (bounds.width / targetBounds.width + bounds.height / targetBounds.height) / 2;

        if (animatingIn)
        {
            if (bounds.width < targetBounds.width || bounds.height < targetBounds.height)
            {
                if (bounds.width >= targetBounds.width * animateInHeightStartPercent)
                    bounds.height = lerp(bounds.height, targetBounds.height, time * heightAnimationSpeed);
                bounds.width = lerp(bounds.width, targetBounds.width, time * widthAnimationSpeed);
            }
        }
        else
        {
            if (bounds.height <= targetBounds.height * (1 - animateOutWidthStartPercent))
                bounds.width = lerp(bounds.width, 0, time * widthAnimationSpeed);
            bounds.height = lerp(bounds.height, 0, time * heightAnimationSpeed);
        }

        bounds.x = targetBounds.x;
        bounds.y = targetBounds.y;
    }

    @Override
    public void render(Painter painter)
    {
        if(bounds.width <= 1.5f && bounds.height <= 1.5f)
            return;

        float centeredX = transform.getPosition().x - (bounds.width / 2);
        float centeredY = transform.getPosition().y - (bounds.height / 2);

        Rectangle.Float centeredRect = new Rectangle.Float(centeredX, centeredY, bounds.width, bounds.height);
        painter.drawAndFillRectangle(centeredRect, boxColor, backgroundColor);
    }

    private float lerp(float start, float end, float t)
    {
        return start + t * (end - start);
    }

    public void animateOut()
    {
        animatingIn = false;
    }
    public void animateIn()
    {
        animatingIn = true;
    }

    public Vector2 getCurrentWidthHeight()
    {
        return new Vector2(bounds.width, bounds.height);
    }
    public Rectangle.Float getCurrentBounds()
    {
        return bounds;
    }

    public Rectangle.Float getTargetBounds()
    {
        return targetBounds;
    }
}
