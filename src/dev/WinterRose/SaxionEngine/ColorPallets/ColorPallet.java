package dev.WinterRose.SaxionEngine.ColorPallets;

import dev.WinterRose.SaxionEngine.Sprite;

import java.awt.*;
import java.util.ArrayList;

public class ColorPallet
{
    private ArrayList<ColorMap> colorMappings = new ArrayList<>();

    public ColorPallet() {}

    /**
     * The two input sprites must be a texture of 1 single row of pixels. the same index of color in each image is mapped.
     * @param from The color in the pallet cha
     * @param to
     */
    public ColorPallet(Sprite from, Sprite to)
    {
        Color[] fromColors = from.getColorData();
        Color[] toColors = to.getColorData();

        if(fromColors.length != toColors.length)
            throw new IllegalStateException("Given sprites do not have the same size. Cant map these two to each other");

        for (int i = 0; i < fromColors.length; i++)
            add(fromColors[i], toColors[i]);
    }
    public ColorPallet(Sprite to)
    {
        this(new Sprite("resources/colorPallets/main.png"), to);
    }

    public void add(Color from, Color to)
    {
        if (get(from) != null)
            throw new IllegalStateException("Color (%s) already added to the map. can not add 2 of the same colors to map to different colors");
        colorMappings.add(new ColorMap(from, to));
    }

    /**
     * Gets the mapped color
     * @param from what color should the mapping of be returned
     * @param fallback if no mapping of the 'from' color exists, return fallback
     * @return the mapped color for 'from' or 'fallback' if no mapping was found*/
    public Color get(Color from, Color fallback)
    {
        for (var map : colorMappings)
        {
            if(map.eval(from))
                return map.to;
        }
        return fallback;
    }

    public Color get(Color from)
    {
        for (var map : colorMappings)
        {
            if(map.eval(from))
                return map.to;
        }
        return null;
    }

    public Color getColorFromIndex(int index) {
        return colorMappings.get(index).to;
    }

    public boolean exists(Color from)
    {
        return get(from) != null;
    }

    private class ColorMap
    {
        public final Color from;
        public final Color to;

        public ColorMap(Color from, Color to)
        {
            this.from = from;
            this.to = to;
        }

        public boolean eval(Color from)
        {
            return this.from.getRGB() == from.getRGB();
        }
    }

}
