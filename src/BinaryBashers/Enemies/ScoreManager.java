package BinaryBashers.Enemies;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static ScoreManager instance;
    private static final String DIRECTORY_PATH = "BinaryBashers/resources/highScores";
    private static final String FILE_NAME = DIRECTORY_PATH + "/highscores.txt";
    private int currentScore;
    private List<Integer> highScores;

    private ScoreManager() {
        this.currentScore = 0;
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
        highScores.add(currentScore);
        highScores.sort(Collections.reverseOrder());
        if (highScores.size() > 20) {
            highScores = highScores.subList(0, 20);
        }
        saveHighScores();
    }

    public List<Integer> getHighScores() {
        return new ArrayList<>(highScores);
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
            for (int i = 0; i < 20; i++) {
                writer.write("0\n");
            }
            System.out.println("Initialized high scores file with default scores.");
        } catch (IOException e) {
            System.err.println("Failed to initialize high scores: " + e.getMessage());
        }
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScores.add(Integer.parseInt(line.trim()));
            }
            highScores.sort(Collections.reverseOrder());
        } catch (IOException e) {
            System.out.println("Error reading high scores file: " + e.getMessage());
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int score : highScores) {
                writer.write(score + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save high scores: " + e.getMessage());
        }
    }
}
