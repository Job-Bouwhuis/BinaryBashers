package dev.WinterRose.SaxionEngine;

import BinaryBashers.UI.TextProviders.DefaultTextProvider;
import BinaryBashers.UI.TextProviders.TextProvider;

import java.awt.*;

public class TextRenderer extends Renderer
{
    private TextProvider text;
    private Vector2 origin;

    public TextRenderer()
    {
        text = new DefaultTextProvider("New Text");
        origin = new Vector2(0.5f, 0.5f);
    }

    public TextRenderer(String text)
    {
        this();
        this.text = new DefaultTextProvider(text);
    }

    public TextProvider getText() {
        return text;
    }

    public void setText(TextProvider text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = new DefaultTextProvider(text);
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public Color getTextColor() {
        return text.getColor();
    }

    public void setTextColor(Color textColor) {
        text.setColor(textColor);
    }

    public FontType getFontType() {
        return text.getFontType();
    }

    public void setFontType(FontType fontType) {
        text.setFontType(fontType);
    }


    @Override
    public void render(Painter painter)
    {
        painter.drawText(text, transform, origin);
    }
}
