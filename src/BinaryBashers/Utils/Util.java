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

    public static Integer calculateBitSize(int num)
    {
        if (num == 0)
            return 1;

        int bitSize = 0;
        while (num > 0)
        {
            num >>= 1;
            bitSize++;
        }
        return bitSize;
    }

    public static Integer decimalToBinary(int num)
    {
        // To store the binary number
        int binaryNumber = 0;
        int cnt = 0;
        while (num != 0)
        {
            int rem = num % 2;
            double c = Math.pow(10, cnt);
            binaryNumber += rem * c;
            num /= 2;

            // Count used to store exponent value
            cnt++;
        }
        return binaryNumber;
    }

    public  static int binaryToDecimal(int num){
        String temp = String.valueOf(num);
        int decimal = 0;
        decimal = Integer.parseInt(temp,2);

        return decimal;
    }

    public static String decimalToHex(int decimal){
        return Integer.toHexString(decimal).toUpperCase();
    }

    public static int hexToDecimal(String hex)
    {
//1A3
        int decimal = 0;
        int length = hex.length();
        int base = 1;

        for (int i = length; i >= 0 ; i--)
        {
            if(hex.charAt(i) >= '0' && hex.charAt(i) <= '9'){
                decimal += (hex.charAt(i) - 48) * base;
                base = base * 16;
            }
            else if(hex.charAt(i) >= 'A' && hex.charAt(i) <= 'F'){}
        }
        return decimal;
    }
}
