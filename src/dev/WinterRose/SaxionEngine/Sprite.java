package dev.WinterRose.SaxionEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

public class Sprite
{
    private Vector2 size;
    private String texturePath;
    private BufferedImage createdImage;

    public Sprite(String path)
    {
        this.texturePath = path;
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        initializeSprite(image);
    }

    public Sprite(BufferedImage image)
    {
        createdImage = image;
        initializeSprite(image);
    }

    public Sprite(Sprite original)
    {

    }

    private void initializeSprite(BufferedImage image)
    {
        if (image != null) size = new Vector2(image.getWidth(), image.getHeight());
        else size = new Vector2(0, 0);
    }

    public static Sprite square(int sizeX, int sizeY, Color color)
    {
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, sizeX, sizeY);
        g.dispose();
        return new Sprite(image);
    }

    public Vector2 getSize()
    {
        return size;
    }

    public String getFilePath()
    {
        if (createdImage != null) return "This image was generated";
        return texturePath;
    }

    public int getwidth()
    {
        return (int) getSize().x;
    }

    public int getHeight()
    {
        return (int) getSize().y;
    }

    /**
     * Gets the image as a BufferedImage, makes a copy of the stored image when called. see getImageRaw for getting the backing image directly
     * @return
     */
    public BufferedImage getImage()
    {
        try
        {
            BufferedImage originalImage;
            if (createdImage != null) originalImage = createdImage;
            else originalImage = ImageIO.read(new File(texturePath));

            if (originalImage != null)
            {
                BufferedImage copy = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = copy.getGraphics();
                g.drawImage(originalImage, 0, 0, null);
                g.dispose();

                // keep track of created image if in case this call was reading from a file.
                // so that getImageRaw can successfully return this exact instance instead of needing to read from the file each time.
                if (createdImage == null) createdImage = copy;

                return copy;
            }
            else
            {
                throw new IOException("Failed to read image for copying: " + texturePath);
            }
        }
        catch (IOException e)
        {
            System.err.println("Error copying image from path: " + texturePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * If this image was generated, it will return the image that is stored instead of making a copy of it. If this is a file reference image, it still loads it into memory
     * @return
     */
    public BufferedImage getImageRaw()
    {
        if (createdImage == null) return getImage();
        return createdImage;
    }

    public Color[] getColorData()
    {
        BufferedImage image = getImageRaw();
        Raster data = image.getData();

        int width = data.getWidth();
        Color[] resultingColors = new Color[width * data.getHeight()];

        for (int i = 0; i < data.getWidth() * data.getHeight(); i++)
        {
            int[] colorValues = new int[4];
            data.getPixel(i % width, i / width, colorValues);
            resultingColors[i] = new Color(colorValues[0], colorValues[1], colorValues[2], colorValues[3]);
        }

        return resultingColors;
    }

    /**
     * Applies the tint color over this sprite.
     *
     * Method was heavily inspired by ChatGPT due to my lack of knowledge in efficient image altering code in java -job
     * @param tint
     * @return A copy of this sprite where the tint color has been applied.
     */
    public Sprite applyTint(Color tint)
    {
        BufferedImage image = getImageRaw();

        float scaleFactorR = tint.getRed() / 255.0f;
        float scaleFactorG = tint.getGreen() / 255.0f;
        float scaleFactorB = tint.getBlue() / 255.0f;
        float[] scales = { scaleFactorR, scaleFactorG, scaleFactorB, 1.0f };
        float[] offsets = { 0.0f, 0.0f, 0.0f, 0.0f };

        RescaleOp op = new RescaleOp(scales, offsets, null);
        return new Sprite(op.filter(image, null));
    }

    public Sprite getSolid()
    {
        final Color transparentColor = new Color(0, 0, 0, 0);
        final Color solidColor = Color.white;
        Color[] spriteColors = getColorData();
        int width = getwidth();

        BufferedImage result = new BufferedImage(getwidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();

        for (int i = 0, spriteColorsLength = spriteColors.length; i < spriteColorsLength; i++)
        {
            if(spriteColors[i].equals(transparentColor))
                continue;

            g.setColor(solidColor);
            int x = i % width;
            int y = i / width;
            g.drawLine(x, y, x, y);
        }

        return new Sprite(result);
    }


}
