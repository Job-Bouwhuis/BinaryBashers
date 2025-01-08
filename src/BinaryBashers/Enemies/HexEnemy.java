package BinaryBashers.Enemies;

import BinaryBashers.Utils.Util;
import dev.WinterRose.SaxionEngine.Vector2;

import java.util.Random;

public class HexEnemy extends Enemy
{
    String hexValue = "";

    public HexEnemy(Integer id, Vector2 enemyPos, Integer difficulty, Boolean fromDecimal)
    {
        super(id, enemyPos, fromDecimal);
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
        hexValue = Util.decimalToHex(decimalNum);
        if (fromDecimal)
        {
            setText(Integer.toString(decimalNum));
        } else
        {
           setText(hexValue);
        }
    }

    @Override
    public boolean compairInput(String input)
    {
        if (input.equals(""))
            return false;

        if (showDecimal)
        {
            //if the hexvalue in decimal is the players decimal input
            int in = Util.hexToDecimal(input);
            return Util.decimalToHex(in).equals(hexValue);
        } else
        {
            //if the players hex input is the decimal input
            int in = Integer.parseInt(input);
            return in == decimalNum;
        }
    }

    @Override
    public int getInputLength()
    {
        if (showDecimal)
        {
            return hexValue.length(); // als hex ingevuld moet worden 'B' = 1 input lengt
        } else
        {
            int count = 0;
            int temp = decimalNum;
            while (temp != 0)
            {
                temp = temp / 10;
                count++;
            }

            return count;
        }
    }

    @Override
    public EnemyFormat getDisplayFormat()
    {
        if(showDecimal)
            return EnemyFormat.Decimal;
        return EnemyFormat.Hex;
    }

    @Override
    public EnemyFormat getInputFormat()
    {
        if (showDecimal)
            return EnemyFormat.Hex;
        return EnemyFormat.Decimal;
    }
}
