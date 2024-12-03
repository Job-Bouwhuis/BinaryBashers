package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.Entities.Enemy;
import dev.WinterRose.SaxionEngine.Vector2;
import BinaryBashers.Utils.Util;

public class BinaryEnemy extends Enemy
{
    private int binaryNum = 0;

    public BinaryEnemy(Integer id, Vector2 enemyPos)
    {
        super(id, enemyPos);
    }

    @Override
    public boolean compairInput(String input)
    {
        int in = Util.binaryToDecimal(Integer.parseInt(input));
        int comparison = Integer.compare(binaryNum, in);
        return comparison == 0;
    }
}