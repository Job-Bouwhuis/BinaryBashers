package dev.WinterRose.SaxionEngine;

import java.awt.*;

public class TextRenderer extends Renderer
{
    public String text;
    public Vector2 origin;
    public Color textColor = Color.WHITE;
    public FontType fontType = FontType.Normal;

    public TextRenderer()
    {
        text = "New Text";
        origin = new Vector2(0.5f, 0.5f);
    }

    public TextRenderer(String text)
    {
        this();
        this.text = text;
    }

    @Override
    public void render(Painter painter)
    {
        painter.drawText(text, transform, origin, textColor, fontType);
    }
}
