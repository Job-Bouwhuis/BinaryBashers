package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

import java.util.Random;

public class BinaryEnemy extends Enemy
{
    private Integer binaryNum = 0;

    public BinaryEnemy(Integer id, Vector2 enemyPos, Integer difficulty, Boolean showDecimal)
    {
        super(id, enemyPos, showDecimal);
        switch (difficulty)
        {
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

        if (showDecimal)
        {
            setText(Integer.toString(decimalNum)); //showing decimal
        } else
        {
            setText(Integer.toString(binaryNum)); // showing binary
        }
    }

    @Override
    public String problem()
    {
        if(showDecimal)
            return Util.binaryToDecimal(binaryNum).toString();

        return binaryNum.toString();
    }

    @Override
    public String answer()
    {
        if(showDecimal)
            return binaryNum.toString();

        return Util.binaryToDecimal(binaryNum).toString();
    }

    @Override
    public EnemyFormat getDisplayFormat()
    {
        if(showDecimal)
            return EnemyFormat.Decimal;
        return EnemyFormat.Binary;
    }

    @Override
    public EnemyFormat getInputFormat()
    {
        if(showDecimal)
            return EnemyFormat.Binary;
        return EnemyFormat.Decimal;
    }

    @Override
    public boolean compairInput(String input)
    {
        if (input.equals(""))
            return false;

        if (showDecimal)
        {
            // compare if the enemy decimal number is correct in Binary
            int in = Integer.parseInt(input);
            return in == binaryNum;

        } else
        {
            // compare if the enemy binary number is correct in decimal based on input
            int in = Integer.parseInt(input);
            return in == decimalNum;
        }
    }


    @Override
    public int getInputLength()
    {
        if(showDecimal)
        {
            return binaryNum.toString().length();
        }

        int count = 0;
        int temp = Util.binaryToDecimal(binaryNum);
        while (temp != 0)
        {
            temp = temp / 10;
            count++;
        }
        return count;
    }
}

