package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

import java.util.Random;

public class BinaryEnemy extends Enemy
{
    private Integer binaryNum = 0;

    public BinaryEnemy(Integer id, Vector2 enemyPos, Integer difficulty)
    {
        super(id, enemyPos);
        switch (difficulty) {
            case 0:
                decimalNum = new Random().nextInt(16);
                break;
            case 1:
                decimalNum = new Random().nextInt(32);
                break;
            case 2:
                decimalNum = new Random().nextInt(64);
            case 3:
                decimalNum = new Random().nextInt(32, 128);
        }
//        decimalNum = new Random().nextInt(16);
        binaryNum = Util.decimalToBinary(decimalNum);
        text = Integer.toString(binaryNum);
    }

    @Override
    public boolean compairInput(String input)
    {
        if(input.equals(""))
            return false;

        int in = Integer.parseInt(input);

        return Util.binaryToDecimal(binaryNum) == in;
    }
    @Override
    public int getInputLength() {

        int count = 0;
        int temp = Util.binaryToDecimal(binaryNum);
        while(temp != 0)
        {
// removing the last digit of the number n
            temp = temp / 10;
// increasing count by 1
            count = count + 1;
        }
        return count;
    }
}

