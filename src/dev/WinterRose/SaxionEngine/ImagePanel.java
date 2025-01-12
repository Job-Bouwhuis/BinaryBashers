package dev.WinterRose.SaxionEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class ImagePanel extends JPanel
{
    private BufferedImage image;

    public ImagePanel(BufferedImage image)
    {
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }

    public ImagePanel()
    {
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
        setSize(image.getWidth(), image.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (image != null)
        {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
