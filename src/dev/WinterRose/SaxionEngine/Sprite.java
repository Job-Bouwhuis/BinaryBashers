package dev.WinterRose.SaxionEngine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    Sprite(BufferedImage image)
    {
        createdImage = image;
        initializeSprite(image);
    }

    private void initializeSprite(BufferedImage image)
    {
        if (image != null)
            size = new Vector2(image.getWidth(), image.getHeight());
        else
            size = new Vector2(0, 0);
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
        if(createdImage != null)
            return "This image was generated";
        return texturePath;
    }
    public int getwidth()
    {
        return (int)getSize().x;
    }
    public int getHeight()
    {
        return (int)getSize().y;
    }

    public Image getImage()
    {
        try
        {
            BufferedImage originalImage;
            if(createdImage != null)
                originalImage = createdImage;
            else
                originalImage = ImageIO.read(new File(texturePath));

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
