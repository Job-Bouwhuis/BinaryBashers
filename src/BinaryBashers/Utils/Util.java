package BinaryBashers.Utils;

public class Util
{

    public static int calculateBitSize(int num)
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

    public static int decimalToBinary(int num)
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
}
