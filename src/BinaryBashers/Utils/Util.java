package BinaryBashers.Utils;

import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.Vector2;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Util
{
    public static Font loadFont(String filePath, float fontSize)
    {
        try
        {
            File fontFile = new File(filePath);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return font.deriveFont(fontSize);
        }
        catch (IOException | FontFormatException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Vector2 screenCenter()
    {
        return new Vector2(Painter.renderWidth / 2, Painter.renderHeight / 2);
    }

    public static int[] decimalToBinary(int num)
    {
        // Creating and assigning binary array size
        int[] binary = new int[35];
        int id = 0;

        // Number should be positive
        while (num > 0)
        {
            binary[id++] = num % 2;
            num = num / 2;
        }

        return binary;
    }
}
