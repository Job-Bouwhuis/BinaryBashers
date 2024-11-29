package dev.WinterRose.SaxionEngine.Entities;

import dev.WinterRose.SaxionEngine.Behavior;
import dev.WinterRose.SaxionEngine.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner extends Behavior {
    private static EnemySpawner instance;
    public static EnemySpawner getInstance() {return instance;}

    private List<Enemy> enemies;
    private float spawnInterval = 30f;
    private float spawnTimer = 0f;
    private Random random;

    public EnemySpawner() {
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }

    @Override public void update() {
        spawnTimer += Time.deltaTime;
        if (spawnTimer >= spawnInterval) {
            spawnEnemy();
            spawnTimer = 0;
        }
    }

    // Spawns an enemy with a random ID
    public void spawnEnemy() {
        int randomId = random.nextInt(3);
        Enemy newEnemy = new Enemy(randomId);
        newEnemy.startAnimation();
        enemies.add(newEnemy);

        System.out.println("Enemy spawned with ID: " + randomId + ". Total enemies: " + enemies.size());
    }

    public void killEnemy(Enemy enemy) {
        enemy.death();
        enemies.remove(enemy);
        System.out.println("Enemy removed. Total enemies: " + enemies.size());
    }

    public boolean hasEnemies() {
        return !enemies.isEmpty();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    @Override public void onDestroyed(){
        instance = null;
    }
    
}
