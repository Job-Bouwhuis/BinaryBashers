package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

import java.util.Random;

public class DecimalEnemy extends Enemy
{
    public DecimalEnemy(Integer id, Vector2 enemyPos, Integer difficulty, Boolean fromDecimal)
    {
        super(id, enemyPos,fromDecimal);
        switch (difficulty)
        {
            case 0:
                decimalNum = new Random().nextInt(16);
                break;
            case 1:
                decimalNum = new Random().nextInt(32);
                break;
            case 2:
                decimalNum = new Random().nextInt(16, 64);
            case 3:
                decimalNum = new Random().nextInt(32, 128);
        }
       setText(Integer.toString(decimalNum));
    }

    @Override
    public String problem()
    {
        if(showDecimal)
            return Util.decimalToBinary(decimalNum).toString();

        return decimalNum.toString();
    }

    @Override
    public String answer()
    {
        if(showDecimal)
            return decimalNum.toString();

        return Util.decimalToBinary(decimalNum).toString();
    }

    @Override
    public boolean compairInput(String input)
    {
        if (input.equals(""))
            return false;

        int in = Integer.parseInt(input);
        return Util.binaryToDecimal(in) == decimalNum;

    }

    @Override
    public int getInputLength()
    {
        return Util.calculateBitSize(decimalNum);
    }

    @Override
    public EnemyFormat getDisplayFormat()
    {
        return EnemyFormat.Decimal;
    }

    @Override
    public EnemyFormat getInputFormat()
    {
        return EnemyFormat.Binary;
    }
}
