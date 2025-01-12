package BinaryBashers.Enemies;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static ScoreManager instance;
    private static final String DIRECTORY_PATH = "BinaryBashers/resources/highScores";
    private static final String FILE_NAME = DIRECTORY_PATH + "/highscores.txt";
    private int currentScore;
    private String currentPlayerName;
    private List<PlayerScore> highScores;
    private int maxScores = 10; // Default maximum number of scores

    private ScoreManager() {
        this.currentScore = 0;
        this.currentPlayerName = "";
        this.highScores = new ArrayList<>();
        createDirectoryAndFile();
        loadHighScores();
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

    public void addPoints(int points) {
        if (points > 0) {
            currentScore += points;
        }
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

    public void saveCurrentScore() {
        if (!currentPlayerName.isEmpty()) {
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
                highScores = highScores.subList(0, maxScores);
            }

            saveHighScores();
        }
    }

    public List<PlayerScore> getHighScores() {
        return new ArrayList<>(highScores);
    }

    public void setMaxScores(int maxScores) {
        this.maxScores = maxScores;
    }

    private void createDirectoryAndFile() {
        File directory = new File(DIRECTORY_PATH);
        File file = new File(FILE_NAME);

        try {
            if (!directory.exists() && directory.mkdirs()) {
                System.out.println("Created directory: " + DIRECTORY_PATH);
            }
            if (!file.exists() && file.createNewFile()) {
                System.out.println("Created file: " + FILE_NAME);
                initializeHighScores();
            }
        } catch (IOException e) {
            System.err.println("Error creating file or directory: " + e.getMessage());
        }
    }

    private void initializeHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Alice,100\n");
            writer.write("Bob,90\n");
            writer.write("Charlie,80\n");
            writer.write("Diana,70\n");
            writer.write("Eve,60\n");
        } catch (IOException e) {
            System.err.println("Failed to initialize high scores: " + e.getMessage());
        }
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
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

        } catch (IOException e) {
            System.out.println("Error reading high scores file: " + e.getMessage());
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (PlayerScore playerScore : highScores) {
                writer.write(playerScore.getName() + "," + playerScore.getScore() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save high scores: " + e.getMessage());
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

        System.out.println("High Scores Before Adding:");
        for (PlayerScore ps : manager.getHighScores()) {
            System.out.println(ps);
        }

        manager.setCurrentPlayerName("Frank");
        manager.resetScore();
        manager.addPoints(85);
        manager.saveCurrentScore();

        System.out.println("High Scores After Adding Frank:");
        for (PlayerScore ps : manager.getHighScores()) {
            System.out.println(ps);
        }

        manager.setCurrentPlayerName("Zara");
        manager.resetScore();
        manager.addPoints(110);
        manager.saveCurrentScore();

        System.out.println("High Scores After Adding Zara:");
        for (PlayerScore ps : manager.getHighScores()) {
            System.out.println(ps);
        }

        manager.setCurrentPlayerName("Tim");
        manager.resetScore();
        manager.addPoints(50);
        manager.saveCurrentScore();

        System.out.println("High Scores After Adding Tim:");
        for (PlayerScore ps : manager.getHighScores()) {
            System.out.println(ps);
        }

        manager.setCurrentPlayerName("Almar");
        manager.resetScore();
        manager.addPoints(100);
        manager.saveCurrentScore();

        System.out.println("High Scores After Adding Almar:");
        for (PlayerScore ps : manager.getHighScores()) {
            System.out.println(ps);
        }
    }
}
