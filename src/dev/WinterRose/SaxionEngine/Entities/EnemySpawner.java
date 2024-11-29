package dev.WinterRose.SaxionEngine.Entities;

import java.util.ArrayList;
import java.util.List;

public class EnemySpawner {
    private List<Enemy> enemies;

    public EnemySpawner() {
        this.enemies = new ArrayList<>();
    }

    public void spawnEnemy() {
        Enemy newEnemy = new Enemy();
        enemies.add(newEnemy);
        
        
        System.out.println("Enemy spawned. Total enemies: " + enemies.size());
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
}
