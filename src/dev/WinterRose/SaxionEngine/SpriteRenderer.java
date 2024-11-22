package dev.WinterRose.SaxionEngine;

public class SpriteRenderer extends Renderer
{
    public Sprite sprite;
    public Vector2 origin;

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
        origin = new Vector2(0.5f, 0.5f);
    }

    @Override
    public void render(Painter painter)
    {
        painter.drawSprite(sprite, transform, sprite.getSize(), origin);
    }
}
