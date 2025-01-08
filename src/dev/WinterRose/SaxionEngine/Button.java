package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;

import java.awt.*;

public class Button extends ActiveRenderer
{
    public Action<Button> onClick = new Action<>();
    public Color normalColor = Color.white;
    public Color hoverColor = Color.lightGray;
    public Color clickColor = new Color(230, 230, 230);
    public Vector2 origin = new Vector2(.5f, .5f);
    public TextProvider text;

    private boolean isHovering = false;
    private boolean isClicking = false;
    private Sprite sprite;
    private Rectangle.Float bounds;

    public Button(Sprite sprite)
    {
        this.sprite = sprite;
        text = new DefaultTextProvider("Button");
        text.setColor(Color.black);
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
    public void awake()
    {
        updateBounds();
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

        Transform textTransform = new Transform();

        float centerX = bounds.x + bounds.width / 2;
        float centerY = bounds.y + bounds.height / 2;
        Vector2 textSize = painter.measureText(text);

        float textX = centerX - textSize.x / 2;
        float textY = centerY - textSize.y / 2;
        textTransform.setPosition(new Vector2(textX, textY - 4));

        painter.drawSprite(sprite, transform, origin, drawingColor);
        painter.drawText(text, textTransform, new Vector2(), Painter.renderBounds);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        sprite = SpritePalletChanger.changePallet(sprite, colorPallet);
    }
}
