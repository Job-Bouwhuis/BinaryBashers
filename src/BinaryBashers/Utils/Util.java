package BinaryBashers.Utils;

public class Util
{
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
