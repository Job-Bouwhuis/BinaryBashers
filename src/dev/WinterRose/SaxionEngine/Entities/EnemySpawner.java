package dev.WinterRose.SaxionEngine.Entities;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner extends ActiveRenderer
{
    /*internal*/ int bok = 0;

    private static EnemySpawner instance;

    public static EnemySpawner getInstance()
    {
        return instance;
    }

    private List<Enemy> enemies;
    private float spawnInterval = 30f;
    private float spawnTimer;
    private Random random;

    public EnemySpawner()
    {
        this.enemies = new ArrayList<>();
        this.random = new Random();
        spawnTimer = Math.max(spawnInterval - 5, 0);
    }

    @Override
    public void update()
    {
        spawnTimer += Time.deltaTime;
        if (spawnTimer >= spawnInterval)
        {
            spawnEnemy();
            spawnTimer = 0;
        }

        for (var e : enemies)
            e.update();
    }

    // Spawns an enemy with a random ID
    public void spawnEnemy()
    {
        if(enemies.size() >= 3)
        {
            System.out.println("Already 3 enemies exist, cant add more");
            return;
        }

        int randomId = random.nextInt(3);
        Vector2 enemyPos = getNewEnemyPosition();

        Enemy newEnemy = new Enemy(randomId, enemyPos);
        newEnemy.startAnimation();
        enemies.add(newEnemy);

        System.out.println("Enemy spawned with ID: " + randomId + ". Total enemies: " + enemies.size());
    }

    private Vector2 getNewEnemyPosition()
    {
        int size = enemies.size();
        if(size == 1)
        {
            return Painter.renderCenter.subtract(new Vector2(Painter.renderCenter.x / 2, 0));
        }
        else if (size == 0)
        {
            return Painter.renderCenter;
        }
        else if(size == 2)
        {
            return Painter.renderCenter.add(new Vector2(Painter.renderCenter.x / 2, 0));
        }
        else
        {
            throw new IllegalStateException("je dikke moeder");
        }
    }

    public void killEnemy(Enemy enemy)
    {
        enemy.death();
        enemies.remove(enemy);
        System.out.println("Enemy removed. Total enemies: " + enemies.size());
    }

    public boolean hasEnemies()
    {
        return !enemies.isEmpty();
    }

    public List<Enemy> getEnemies()
    {
        return enemies;
    }

    @Override
    public void onDestroyed()
    {
        instance = null;
    }

    @Override
    public void render(Painter painter)
    {
        for (var e : enemies)
            e.render(painter);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        for(var e : enemies)
            e.getSprite().onColorPalleteChange(colorPallet);
    }
}
