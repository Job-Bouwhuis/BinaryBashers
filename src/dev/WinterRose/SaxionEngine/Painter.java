package dev.WinterRose.SaxionEngine;

import nl.saxion.app.SaxionApp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class Painter
{
    private BufferedImage renderCanvas;
    private Graphics2D graphics;
    private boolean isStarted = false;

    Painter() { }

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

            if (!constructor.canAccess(null))
            {
                constructor.setAccessible(true);
            }

            return constructor.newInstance(image, x, y, width, height);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create an Image instance", e);
        }
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
        graphics.fillRect((int)position.x, (int)position.y, scale.x, scale.y);
    }

    /**
     * Renders a circle to the screen on the given position
     * @param position
     * @param radius
     * @param color
     */
    public void drawCircle(Vector2 position, int radius, Color color)
    {
        ensureStarted();
        graphics.setColor(color);
        graphics.fillOval((int)position.x - radius, (int)position.y - radius, radius * 2, radius * 2);
    }

    /**
     * Renders the given sprite at the given position, with the given scale. (1, 1) scale renders the sprite at the image width x height.
     */
    public void drawSprite(Sprite sprite, Transform transform, Vector2 size, Vector2 origin)
    {
        ensureStarted();

        Vector2 originRelativePosition = CalculateOrigin(transform, size, origin);

        // learned existence of AffineTransform from ChatGPT. code itself written manually
        // used chatGPT to search the internet to ways to use a double or float for positional and
        // scaling values using java's build in Graphics2D class
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(originRelativePosition.x, originRelativePosition.y);

        Vector2 rotationAnchor = new Vector2(size.x * origin.x, size.y * origin.y);

        double rotationRadians = transform.getRotation().getRadians();
        affineTransform.rotate(rotationRadians, rotationAnchor.x, rotationAnchor.y);

        affineTransform.scale(transform.getScale().x, transform.getScale().y);

        graphics.drawImage(sprite.getImage(), affineTransform, null);
    }

    public void drawText(String text, Transform transform, Vector2 origin, Color color)
    {
        DrawableCharacter[] characters = new DrawableCharacter[text.length()];
        for(int i = 0; i < text.length(); i++)
            characters[i] = new DrawableCharacter(text.charAt(i), color);

        drawTextInternal(characters, text, transform, origin);
    }

    public void drawText(DrawableCharacter[] characters, Transform transform, Vector2 origin)
    {
        String text = getStringFromDrawableCharacters(characters);
        drawTextInternal(characters, text, transform, origin);
    }

    private void drawTextInternal(DrawableCharacter[] characters, String text, Transform transform, Vector2 origin)
    {
        Vector2 position = transform.getPosition();
        Vector2 scale = transform.getScale();
        Rotation rotation = transform.getRotation();



        int fontSize = (int)(graphics.getFont().getSize() * scale.y);
        graphics.setFont(new Font(graphics.getFont().getFontName(), Font.PLAIN, fontSize));

        FontMetrics metric = graphics.getFontMetrics(graphics.getFont());

        int textHeight = metric.getAscent() - metric.getDescent();
        float adjustedX = position.x - metric.stringWidth(text) * origin.x;
        float adjustedY = position.y - textHeight * origin.y;

        Color prevColor = graphics.getColor();
        AffineTransform beforeTransform = graphics.getTransform();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(adjustedX, adjustedY);
        Vector2 rotationAnchor = new Vector2(metric.stringWidth(text) * origin.x, textHeight * origin.y);
        affineTransform.rotate(rotation.getRadians(), rotationAnchor.x, rotationAnchor.y);
        affineTransform.scale(scale.x, scale.y);

        graphics.setTransform(affineTransform);

        float xOffset = 0;
        int padding = 1;
        for (DrawableCharacter c : characters)
        {
            String character = String.valueOf(c.character);
            graphics.setColor(c.color);
            graphics.drawString(character, (int)xOffset, 0);
            xOffset += metric.stringWidth(character) + padding;
        }

        graphics.setTransform(beforeTransform);
        graphics.setColor(prevColor);
    }

    private String getStringFromDrawableCharacters(DrawableCharacter[] characters)
    {
        StringBuilder sb = new StringBuilder();

        for(var c : characters)
            sb.append(c.character);

        return sb.toString();
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
        if (!isStarted)
            throw new IllegalStateException("Frame not yet started. Call start() first.");
    }
}
