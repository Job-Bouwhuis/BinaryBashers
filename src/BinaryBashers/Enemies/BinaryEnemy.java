package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

import java.util.Random;

public class BinaryEnemy extends Enemy
{
    private Integer binaryNum = 0;
    private Integer decimalNum = 0;
    public BinaryEnemy(Integer id, Vector2 enemyPos)
    {
        super(id, enemyPos);
        decimalNum = new Random().nextInt(16);
        text = Integer.toString(decimalNum);
        binaryNum = Util.decimalToBinary(decimalNum);
    }

    @Override
    public boolean compairInput(String input)
    {
        if(input.equals(""))
            return false;

        int in = Integer.parseInt(input);

        int comparison = Integer.compare(binaryNum, in);
        return comparison == 0;
    }

}