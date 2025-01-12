package BinaryBashers.Enemies;

public class DifficultyGenerator {
   final int[] scoreDifficultyValues = new int[]{
        2, 10, 20, 30, 40
   };

    public int getDifficultyNumber(int score) {
        System.out.println(score);
        for (int i = 0; i < scoreDifficultyValues.length; i++) {
            if (score < scoreDifficultyValues[i]) {
                return i;
            }
        }
        // Should never be returned
        return 0;
    }
}
