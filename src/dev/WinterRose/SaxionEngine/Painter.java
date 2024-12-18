package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class Painter
{
    public static final int renderWidth = 640;
    public static final int renderHeight = 360;
    public static final Rectangle2D.Float renderBounds = new Rectangle2D.Float(0, 0, renderWidth, renderHeight);
    public static final Vector2 renderCenter = new Vector2(renderWidth / 2, renderHeight / 2);

    private BufferedImage renderCanvas;
    private Graphics2D graphics;
    private boolean isStarted = false;

    private TintedSpriteCache tintedSpriteCache = new TintedSpriteCache();

    /*internal*/ Painter() {}

    /**
     * This is a rather expensive call relatively speaking. Do not call repeatedly in the Update loop.
     * @param text
     * @return a Vector2 representing the size of the text when drawn using Painter.drawText
     */
    public static Vector2 measureString(String text)
    {
        var chars = getDrawableCharactersFromString(text, Color.white);

        var img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        var graphics = img.createGraphics();
        Font font = graphics.getFont();
        FontMetrics fontMetrics = graphics.getFontMetrics(font);

        float width = 0;
        float height = fontMetrics.getAscent() - fontMetrics.getDescent();
        ;

        final float padding = 1;

        for (var c : chars)
            width += fontMetrics.stringWidth("" + c.character) + padding;

        return new Vector2(width, height);
    }

    /**
     * Workaround to still be able to make an instance of the SaxionApp Image class, since this is a SaxionApp.Drawiable kind. which
     * SaxionApp needs to draw things to its magical window.
     * Due to our rescaling feature where the game is rendered at a lower resolution, and then upscaled we need this specific constructor to get this working
     */
    private static nl.saxion.app.canvas.drawable.Image createSaxionImage(BufferedImage image, int x, int y, int width, int height)
    {
        try
        {
            Constructor<nl.saxion.app.canvas.drawable.Image> constructor = nl.saxion.app.canvas.drawable.Image.class.getDeclaredConstructor(BufferedImage.class, int.class, int.class, int.class, int.class);

            if (!constructor.canAccess(null)) constructor.setAccessible(true);

            return constructor.newInstance(image, x, y, width, height);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create an Image instance", e);
        }
    }

    private static DrawableCharacter[] getDrawableCharactersFromString(String text, Color color)
    {
        DrawableCharacter[] characters = new DrawableCharacter[text.length()];
        for (int i = 0; i < text.length(); i++)
            characters[i] = new DrawableCharacter(text.charAt(i), color);
        return characters;
    }

    public boolean hasStarted()
    {
        return isStarted;
    }

    /**
     * Begins a new frame. and make Painter ready to draw this new frame
     */
    public void begin()
    {
        if (isStarted) throw new IllegalStateException("Frame already started. Call end() before starting again.");

        renderCanvas = new BufferedImage(640, 360, BufferedImage.TYPE_INT_ARGB);
        graphics = renderCanvas.createGraphics();
        isStarted = true;
    }

    /**
     * Renders a square on the given position
     * @param position
     * @param scale
     * @param color
     */
    public void drawSquare(Vector2 position, Point scale, Color color)
    {
        ensureStarted();
        graphics.setColor(color);
        graphics.fillRect((int) position.x, (int) position.y, scale.x, scale.y);
    }

    public void drawRectangle(Rectangle2D rect, Color color)
    {
        Color prevColor = graphics.getColor();
        ensureStarted();
        graphics.setColor(color);
        graphics.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        graphics.setColor(prevColor);
    }

    public void drawAndFillRectangle(Rectangle2D rect, Color borderColor, Color fillColor)
    {
        ensureStarted();

        // inside
        Color prevColor = graphics.getColor();
        graphics.setColor(fillColor);
        graphics.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        graphics.setColor(prevColor);

        // outside
        drawRectangle(rect, borderColor);
    }

    //    public void drawText(TextProvider text, Transform transform, Vector2 origin)
    //    {
    //        drawTextInternal(text.getCharacters(), text.getText(), transform, origin, text.getFontType());
    //    }

    public void drawText(String text, Transform transform, Vector2 origin, Color color, FontType fontType)
    {
        drawTextInternal(getDrawableCharactersFromString(text, color), text, transform, origin, fontType);
    }

    public void drawText(DrawableCharacter[] characters, Transform transform, Vector2 origin, FontType fontType)
    {
        String text = EngineUtil.getStringFromDrawableCharacters(characters);
        drawTextInternal(characters, text, transform, origin, fontType);
    }

    /**
     * Renders a circle to the screen on the given position
     * @param position
     * @param radius
     * @param color
     */
    public void drawCircle(Vector2 position, int radius, Color color)
    {
        Color prevColor = graphics.getColor();
        ensureStarted();
        graphics.setColor(color);
        graphics.fillOval((int) position.x - radius, (int) position.y - radius, radius * 2, radius * 2);
        graphics.setColor(prevColor);
    }

    /**
     * Renders the given sprite at the given position, with the given scale. (1, 1) scale renders the sprite at the image width x height.
     */
    public void drawSprite(Sprite sprite, Transform transform, Vector2 origin, Color tint)
    {
        ensureStarted();

        Vector2 size = sprite.getSize();
        Vector2 originRelativePosition = CalculateOrigin(transform, size, origin);

        Vector2 parentPosition = transform.getParent() != null ? transform.getParent()
                .getWorldPosition() : transform.getWorldPosition();
        originRelativePosition.add(parentPosition);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(originRelativePosition.x, originRelativePosition.y);

        Vector2 rotationAnchor = new Vector2(size.x * origin.x, size.y * origin.y);

        double rotationRadians = transform.getRotationRadians();
        affineTransform.rotate(rotationRadians, rotationAnchor.x, rotationAnchor.y);

        affineTransform.scale(transform.getScale().x, transform.getScale().y);

        if (tint.equals(Color.white))
        {
            graphics.drawImage(sprite.getImageRaw(), affineTransform, null);
            return;
        }
        BufferedImage image = tintedSpriteCache.getTintedSprite(sprite, tint).getImageRaw();
        graphics.drawImage(image, affineTransform, null);
    }


    public void drawText(TextProvider text, Transform transform, Vector2 origin, Rectangle.Float bounds)
    {
        ensureStarted();
        Vector2 position = transform.getPosition();
        Vector2 scale = transform.getScale();
        float rotationRads = transform.getRotationRadians();

        Font prevFont = graphics.getFont();
        Color prevColor = graphics.getColor();
        AffineTransform beforeTransform = graphics.getTransform();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(position.x, position.y);
        affineTransform.rotate(rotationRads, bounds.width * origin.x, bounds.height * origin.y);
        affineTransform.scale(scale.x, scale.y);
        graphics.setTransform(affineTransform);

        float xOffset = 0;
        float yOffset = 0;

        for (TextProvider.Word word : text.getWords())
        {
            Font wordFont = word.font != null ? word.font : new Font(graphics.getFont()
                    .getFontName(), switch (word.fontType) {
                case Normal -> Font.PLAIN;
                case Italic -> Font.ITALIC;
                case Bold -> Font.BOLD;
            }, (int) (graphics.getFont()
                    .getSize() * scale.y));
            graphics.setFont(wordFont);
            FontMetrics metric = graphics.getFontMetrics(wordFont);

            int wordWidth = 0;
            for (DrawableCharacter c : word.getCharacters())
                wordWidth += metric.charWidth(c.character) + text.getCharacterXPadding();
            wordWidth += metric.charWidth(' ') * (word.paddingChar != null ? 1 : 0);

            int lineHeight = metric.getAscent() - metric.getDescent();

            if (xOffset + wordWidth > bounds.width)
            {
                xOffset = 0;
                yOffset += lineHeight + text.getCharacterYPadding();

                if (yOffset + lineHeight > bounds.height)
                    break;
            }

            for (DrawableCharacter c : word.getCharacters())
            {
                String character = String.valueOf(c.character);
                graphics.setColor(c.color);
                graphics.drawString(character, (int) (bounds.x + xOffset), (int) (bounds.y + yOffset + metric.getAscent()));
                xOffset += metric.charWidth(c.character) + text.getCharacterXPadding();
            }

            if (word.paddingChar != null)
            {
                if (word.paddingChar == '\n')
                {
                    xOffset = 0;
                    yOffset += lineHeight + text.getCharacterYPadding();

                    if (yOffset + lineHeight > bounds.height)
                        break;
                }
                else
                    xOffset += metric.charWidth(word.paddingChar) + text.getCharacterXPadding();
            }

            graphics.setColor(prevColor);
            graphics.setFont(prevFont);
        }

        graphics.setTransform(beforeTransform);
    }

    public void drawText(String text, Vector2 position, Vector2 origin)
    {
        drawText(text, position, origin, Color.white);
    }

    public void drawText(String text, Vector2 position, Vector2 origin, Color color)
    {
        drawText(text, position, origin, color, FontType.Normal);
    }

    public void drawText(String text, Vector2 position, Vector2 origin, Color color, FontType fontType)
    {
        drawScaledText(text, position, new Vector2(1), origin, color, fontType);
    }

    public void drawScaledText(String text, Vector2 position, Vector2 scale, Vector2 origin, Color color, FontType fontType)
    {
        Transform t = new Transform();
        t.setPosition(position);
        t.setScale(scale);

        TextProvider textprov = new DefaultTextProvider();
        textprov.setTextColorAndFontType(text, color, fontType);

        drawText(textprov, t, origin, renderBounds);
    }

    private void drawTextInternal(DrawableCharacter[] characters, String text, Transform transform, Vector2 origin, FontType fontType)
    {
        Vector2 position = transform.getPosition();
        Vector2 scale = transform.getScale();
        float rotationRads = transform.getRotationRadians();

        var prevFont = graphics.getFont();

        int fontSize = (int) (graphics.getFont().getSize() * scale.y);
        int fType = switch (fontType)
        {
            case Normal -> Font.PLAIN;
            case Bold -> Font.BOLD;
            case Italic -> Font.ITALIC;
        };
        graphics.setFont(new Font(graphics.getFont().getFontName(), fType, fontSize));

        FontMetrics metric = graphics.getFontMetrics(graphics.getFont());

        int textHeight = metric.getAscent() - metric.getDescent();
        float adjustedX = position.x - metric.stringWidth(text) * origin.x;
        float adjustedY = position.y - textHeight * origin.y;

        Color prevColor = graphics.getColor();
        AffineTransform beforeTransform = graphics.getTransform();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(adjustedX, adjustedY);
        Vector2 rotationAnchor = new Vector2(metric.stringWidth(text) * origin.x, textHeight * origin.y);
        affineTransform.rotate(rotationRads, rotationAnchor.x, rotationAnchor.y);
        affineTransform.scale(scale.x, scale.y);

        graphics.setTransform(affineTransform);

        float xOffset = 0;
        int padding = 1;
        for (DrawableCharacter c : characters)
        {
            String character = String.valueOf(c.character);
            graphics.setColor(c.color);
            graphics.drawString(character, (int) xOffset, 0);
            xOffset += metric.stringWidth(character) + padding;
        }

        graphics.setTransform(beforeTransform);
        graphics.setColor(prevColor);
        graphics.setFont(prevFont);
    }

    private Vector2 CalculateOrigin(Transform transform, Vector2 size, Vector2 origin)
    {
        float originX = (size.x * transform.getScale().x) * origin.x;
        float originY = (size.y * transform.getScale().y) * origin.y;

        float newX = transform.getPosition().x - originX;
        float newY = transform.getPosition().y - originY;
        return new Vector2(newX, newY);
    }

    /**
     * Ends the drawing and submits the current requests to the screen. Auto scales the render resolution to the window resolution without creating a blurry image.
     */
    public void end()
    {
        ensureStarted();

        int windowWidth = SaxionApp.getWidth();
        int windowHeight = SaxionApp.getHeight();

        int targetWidth = windowWidth;
        int targetHeight = (int) (windowWidth / 16.0 * 9);

        if (targetHeight > windowHeight)
        {
            targetHeight = windowHeight;
            targetWidth = (int) (windowHeight / 9.0 * 16);
        }

        BufferedImage scaledCanvas = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D scaledGraphics = scaledCanvas.createGraphics();

        scaledGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        scaledGraphics.drawImage(renderCanvas, 0, 0, targetWidth, targetHeight, null);
        scaledGraphics.dispose();

        var saxionImage = createSaxionImage(scaledCanvas, 0, 0, targetWidth, targetHeight);
        nl.saxion.app.SaxionApp.clear();
        SaxionApp.add(saxionImage);

        graphics.dispose();
        renderCanvas = null;
        graphics = null;
        isStarted = false;
    }

    /**
     * Helper method to that throws if the painter has not yet started.
     */
    private void ensureStarted()
    {
        if (!isStarted) throw new IllegalStateException("Frame not yet started. Call start() first.");
    }

    public Vector2 measureText(TextProvider text)
    {
        return measureString(text.getText());
    }
}
