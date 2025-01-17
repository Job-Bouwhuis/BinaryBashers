package BinaryBashers.Enemies;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager
{
    private static final String DIRECTORY_PATH = "resources/highScores";
    private static final String FILE_NAME_TEMPLATE = DIRECTORY_PATH + "/level_%d_highscores.scores";
    private static ScoreManager instance;
    private int currentScore;
    private String currentPlayerName;
    private Map<Integer, ArrayList<PlayerScore>> levelHighScores;
    private int maxScores = 100;

    private ScoreManager()
    {
        this.currentScore = 0;
        this.currentPlayerName = "";
        this.levelHighScores = new HashMap<>();
        createDirectory();
    }

    public static ScoreManager getInstance()
    {
        if (instance == null)
        {
            instance = new ScoreManager();
        }
        return instance;
    }

    public int getCurrentScore()
    {
        return currentScore;
    }

    public String getCurrentPlayerName()
    {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String name)
    {
        currentPlayerName = name.trim();
    }

    public void addPoints(int timeTaken)
    {
        int points = Math.max(1, 10 - timeTaken);
        currentScore += points;
        System.out.println("Points added: " + points + " (Time taken: " + timeTaken + "s)");
    }

    public void subtractPoints(int points)
    {
        if (points > 0)
        {
            currentScore -= points;
            if (currentScore < 0) currentScore = 0;
        }
    }

    public void resetScore()
    {
        currentScore = 0;
    }

    public ArrayList<PlayerScore> getScores(int level)
    {
        loadHighScores(level);
        return levelHighScores.get(level);
    }

    public void saveCurrentScore(int level)
    {
        saveCurrentScore(level, true);
    }

    public void saveCurrentScore(int level, boolean reFetch)
    {
        if (!currentPlayerName.isEmpty())
        {
            levelHighScores.putIfAbsent(level, new ArrayList<>());

            ArrayList<PlayerScore> highScores = getScores(level);
            highScores.add(new PlayerScore(currentPlayerName, currentScore));
            highScores.sort((score1, score2) -> Integer.compare(score2.score, score1.score));

            if (highScores.size() > maxScores)
            {
                highScores = new ArrayList<>(highScores.subList(0, maxScores));
                levelHighScores.put(level, highScores);
            }

            saveHighScores(level);
        }
    }

    public ArrayList<PlayerScore> getHighScores(int level)
    {
        levelHighScores.putIfAbsent(level, new ArrayList<>());
        return new ArrayList<>(levelHighScores.get(level));
    }

    public void setMaxScores(int maxScores)
    {
        this.maxScores = maxScores;
    }

    private void createDirectory()
    {
        File directory = new File(DIRECTORY_PATH);

        try
        {
            if (!directory.exists() && directory.mkdirs()) System.out.println("Created directory: " + DIRECTORY_PATH);
        }
        catch (Exception e)
        {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    private void loadHighScores(int level)
    {
        levelHighScores.remove(level);

        File file = new File(String.format(FILE_NAME_TEMPLATE, level));
        ArrayList<PlayerScore> highScores = new ArrayList<>();

        if (!file.exists())
        {
            try
            {
                if (!file.createNewFile())
                    System.out.println("Error creating scores file");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");
                if (parts.length == 2)
                {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    highScores.add(new PlayerScore(name, score));
                }
            }

            levelHighScores.put(level, highScores);
        }
        catch (IOException e)
        {
            System.out.println("Error reading high scores file for level " + level + ": " + e.getMessage());
        }
    }

    private void saveHighScores(int level)
    {
        File file = new File(String.format(FILE_NAME_TEMPLATE, level));
        ArrayList<PlayerScore> highScores = levelHighScores.get(level);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
        {
            for (PlayerScore playerScore : highScores)
                writer.write(playerScore.getName() + "," + playerScore.getScore() + "\n");
        }
        catch (IOException e)
        {
            System.err.println("Failed to save high scores for level " + level + ": " + e.getMessage());
        }
    }

    public void overrideScore(int level, String name)
    {
        ArrayList<PlayerScore> scores = getScores(level);

        for (PlayerScore score : scores)
        {
            if (score.name.equals(name))
            {
                score.score = currentScore;
                break;
            }
        }
        ArrayList<PlayerScore> highScores = levelHighScores.get(level);
        highScores.sort((score1, score2) -> Integer.compare(score2.score, score1.score));
        levelHighScores.put(level, highScores);

        saveHighScores(level);
    }

    public static class PlayerScore
    {
        private final String name;
        private int score;

        public PlayerScore(String name, int score)
        {
            this.name = name;
            this.score = score;
        }

        public String getName()
        {
            return name;
        }

        public int getScore()
        {
            return score;
        }

        @Override
        public String toString()
        {
            return name + " - " + score;
        }
    }
}
