package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;

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

    public TextRenderer(TextProvider text)
    {
        this();
        this.text = text;
    }

    public TextProvider getText() {
        return text;
    }

    public void setText(TextProvider text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text.setText(text);
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
        painter.drawText(text, transform, origin, Painter.renderBounds);
    }

    public TextProvider getTextProvider()
    {
        return text;
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        text.setColor(colorPallet.getColorFromIndex(6));
    }
}
