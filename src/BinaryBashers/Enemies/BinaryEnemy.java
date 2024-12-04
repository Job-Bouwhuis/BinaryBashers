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
    @Override
    public int getInputLength() {
        return Util.calculateBitSize(decimalNum);
    }
}