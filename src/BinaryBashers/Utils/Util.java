package BinaryBashers.Utils;

import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.Vector2;

public class Util
{
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
        while (num > 0) {
            binary[id++] = num % 2;
            num = num / 2;
        }

        return binary;
    }
}
