package BinaryBashers.Enemies;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static ScoreManager instance;
    private static final String DIRECTORY_PATH = "BinaryBashers/resources/highScores";
    private static final String FILE_NAME_TEMPLATE = DIRECTORY_PATH + "/level_%d_highscores.txt";
    private int currentScore;
    private String currentPlayerName;
    private Map<Integer, List<PlayerScore>> levelHighScores;
    private int maxScores = 10; // Default maximum number of scores

    private ScoreManager() {
        this.currentScore = 0;
        this.currentPlayerName = "";
        this.levelHighScores = new HashMap<>();
        createDirectory();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            currentPlayerName = name.trim();
        }
    }

    public void addPoints(int timeTaken) {
        int points = Math.max(1, 10 - timeTaken);
        currentScore += points;
        System.out.println("Points added: " + points + " (Time taken: " + timeTaken + "s)");
    }


    public void subtractPoints(int points) {
        if (points > 0) {
            currentScore -= points;
            if (currentScore < 0) {
                currentScore = 0;
            }
        }
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void saveCurrentScore(int level) {
        if (!currentPlayerName.isEmpty()) {
            levelHighScores.putIfAbsent(level, new ArrayList<>());
            List<PlayerScore> highScores = levelHighScores.get(level);

            // Add current score to the list
            highScores.add(new PlayerScore(currentPlayerName, currentScore));

            // Sort scores manually in descending order using a for loop
            for (int i = 0; i < highScores.size() - 1; i++) {
                for (int j = i + 1; j < highScores.size(); j++) {
                    if (highScores.get(i).getScore() < highScores.get(j).getScore()) {
                        PlayerScore temp = highScores.get(i);
                        highScores.set(i, highScores.get(j));
                        highScores.set(j, temp);
                    }
                }
            }

            // Trim to maxScores
            if (highScores.size() > maxScores) {
                highScores = new ArrayList<>(highScores.subList(0, maxScores));
                levelHighScores.put(level, highScores);
            }

            saveHighScores(level);
        }
    }

    public List<PlayerScore> getHighScores(int level) {
        levelHighScores.putIfAbsent(level, new ArrayList<>());
        return new ArrayList<>(levelHighScores.get(level));
    }

    public void setMaxScores(int maxScores) {
        this.maxScores = maxScores;
    }

    private void createDirectory() {
        File directory = new File(DIRECTORY_PATH);

        try {
            if (!directory.exists() && directory.mkdirs()) {
                System.out.println("Created directory: " + DIRECTORY_PATH);
            }
        } catch (Exception e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    private void initializeHighScores(int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(FILE_NAME_TEMPLATE, level)))) {
            writer.write("Alice,100\n");
            writer.write("Bob,90\n");
            writer.write("Charlie,80\n");
            writer.write("Diana,70\n");
            writer.write("Eve,60\n");
        } catch (IOException e) {
            System.err.println("Failed to initialize high scores for level " + level + ": " + e.getMessage());
        }
    }

    private void loadHighScores(int level) {
        File file = new File(String.format(FILE_NAME_TEMPLATE, level));
        List<PlayerScore> highScores = new ArrayList<>();

        if (!file.exists()) {
            initializeHighScores(level);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    highScores.add(new PlayerScore(name, score));
                }
            }

            // Sort scores manually in descending order using a for loop
            for (int i = 0; i < highScores.size() - 1; i++) {
                for (int j = i + 1; j < highScores.size(); j++) {
                    if (highScores.get(i).getScore() < highScores.get(j).getScore()) {
                        PlayerScore temp = highScores.get(i);
                        highScores.set(i, highScores.get(j));
                        highScores.set(j, temp);
                    }
                }
            }

            levelHighScores.put(level, highScores);
        } catch (IOException e) {
            System.out.println("Error reading high scores file for level " + level + ": " + e.getMessage());
        }
    }

    private void saveHighScores(int level) {
        File file = new File(String.format(FILE_NAME_TEMPLATE, level));
        List<PlayerScore> highScores = levelHighScores.get(level);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (PlayerScore playerScore : highScores) {
                writer.write(playerScore.getName() + "," + playerScore.getScore() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save high scores for level " + level + ": " + e.getMessage());
        }
    }

    private static class PlayerScore implements Comparable<PlayerScore> {
        private final String name;
        private final int score;

        public PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(PlayerScore other) {
            return Integer.compare(other.score, this.score); // Descending order
        }

        @Override
        public String toString() {
            return name + " - " + score;
        }
    }

    public static void testScoreManager() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.setMaxScores(10); // Setting max scores to 10 for testing

        System.out.println("High Scores Before Adding to Level 1:");
        manager.loadHighScores(1); // Remember to load the scores otherwise it will just overwrite! // NOTE: I think we could fix this by putting the loading part into the saving, but i'm too tired rn.
        for (PlayerScore ps : manager.getHighScores(1)) {
            System.out.println(ps);
        }

        manager.setCurrentPlayerName("Frank");
        manager.resetScore();
        manager.addPoints(85);
        manager.saveCurrentScore(1);

        System.out.println("High Scores After Adding Frank to Level 1:");
        for (PlayerScore ps : manager.getHighScores(1)) {
            System.out.println(ps);
        }

        manager.loadHighScores(2);


        manager.setCurrentPlayerName("Zara");
        manager.resetScore();
        manager.addPoints(110);
        manager.saveCurrentScore(2);

        manager.setCurrentPlayerName("Frank");
        manager.resetScore();
        manager.addPoints(85);
        manager.saveCurrentScore(2);

        System.out.println("High Scores After Adding Zara to Level 2:");
        manager.loadHighScores(2);
        for (PlayerScore ps : manager.getHighScores(2)) {
            System.out.println(ps);
        }
    }
}
