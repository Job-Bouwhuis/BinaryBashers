package dev.WinterRose.SaxionEngine.ColorPallets;

import java.awt.*;
import java.util.ArrayList;

public class ColorPallet
{
    private ArrayList<ColorMap> colorMappings = new ArrayList<>();

    public void add(Color from, Color to)
    {
        if (get(from) != null)
            throw new IllegalStateException("Color (%s) already added to the map. can not add 2 of the same colors to map to different colors");
        colorMappings.add(new ColorMap(from, to));
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
