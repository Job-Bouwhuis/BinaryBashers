package dev.WinterRose.SaxionEngine;

import java.awt.*;

public class Button extends ActiveRenderer
{
    public Action<Button> onClick = new Action<>();
    public Color normalColor = Color.white;
    public Color hoverColor = Color.yellow;
    public Color clickColor = Color.blue;
    public Vector2 origin = new Vector2(.5f, .5f);

    private boolean isHovering = false;
    private boolean isClicking = false;
    private Sprite sprite;
    private Rectangle.Float bounds;


    public Button(Sprite sprite)
    {
        this.sprite = sprite;
    }

    private void updateBounds()
    {
        var size = sprite.getSize();
        var scale = transform.getScale();
        var pos = transform.getWorldPosition();
        if (bounds == null) bounds = new Rectangle.Float(pos.x, pos.y, size.x * scale.x, size.y * scale.y);
        else bounds.setRect(pos.x - ((size.x / scale.x) / 2), pos.y - ((size.y / scale.y) / 2), size.x * scale.x, size.y * scale.y);
    }

    @Override
    public void update()
    {
        updateBounds();
        var mousePos = Input.getMousePosition();
        if (bounds.contains(mousePos.x, mousePos.y))
        {
            isHovering = true;
            if (Input.getMouse(MouseButton.Left)) isClicking = true;
            else isClicking = false;

            if (Input.getMouseUp(MouseButton.Left))
            {
                onClick.invoke(this);
            }
        }
        else
        {
            isClicking = false;
            isHovering = false;
        }
    }

    @Override
    public void render(Painter painter)
    {
        Color drawingColor = normalColor;
        if(isHovering)
            drawingColor = hoverColor;
        if(isClicking)
            drawingColor = clickColor;

        painter.drawSprite(sprite, transform, origin, drawingColor);
    }
}
