package dev.WinterRose.SaxionEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite
{
    public final int width;
    public final int height;
    private final Vector2 size;
    public String texturePath;

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

        if (image != null)
        {
            this.width = image.getWidth();
            this.height = image.getHeight();
            size = new Vector2(width, height);
        }
        else
        {
            width = 0;
            height = 0;
            size = new Vector2(0, 0);
        }
    }

    public static Sprite Square(int sizeX, int sizeY, Color color)
    {
        String cacheFolder = "CachedSprites";
        File folder = new File(cacheFolder);
        if (!folder.exists())
        {
            folder.mkdirs();
        }

        String fileName = String.format("%s/Square_%dx%d_%d_%d_%d.png", cacheFolder, sizeX, sizeY, color.getRed(), color.getGreen(), color.getBlue());
        File file = new File(fileName);

        if (!file.exists())
        {
            try
            {
                BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setColor(color);
                g.fillRect(0, 0, sizeX, sizeY);
                g.dispose();

                ImageIO.write(image, "png", file);
            }
            catch (IOException e)
            {
                System.err.println("Error creating square texture: " + fileName);
                e.printStackTrace();
                return null;
            }
        }

        return new Sprite(file.getPath());
    }

    public Vector2 getSize()
    {
        return size;
    }

    public Image getImage()
    {
        try
        {
            BufferedImage originalImage = ImageIO.read(new File(texturePath));
            if (originalImage != null)
            {
                BufferedImage copy = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = copy.getGraphics();
                g.drawImage(originalImage, 0, 0, null);
                g.dispose();
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
}
