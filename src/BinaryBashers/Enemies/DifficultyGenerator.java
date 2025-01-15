package BinaryBashers.Enemies;

public class DifficultyGenerator
{
    final int[] scoreDifficultyValues = new int[]{
            30, 60, 90, 120 };

    public int getDifficultyNumber(int score)
    {
        System.out.println(score);
        for (int i = 0; i < scoreDifficultyValues.length; i++)
        {
            if (score < scoreDifficultyValues[i])
            {
                System.out.println("On Difficulty: " + i);
                return i;
            }
        }
        // Should never be returned
        return 0;
    }
}
