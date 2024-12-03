package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

import java.util.Random;

public class BinaryEnemy extends Enemy
{
    private int binaryNum = 0;

    public BinaryEnemy(Integer id, Vector2 enemyPos)
    {
        super(id, enemyPos);
        binaryNum = new Random().nextInt(16);
    } //randomize binary num

    @Override
    public boolean compairInput(String input)
    {
        if(input.equals(""))
            return false;

        int in = Util.binaryToDecimal(Integer.parseInt(input));
        int comparison = Integer.compare(binaryNum, in);
        return comparison == 0;
    }

}