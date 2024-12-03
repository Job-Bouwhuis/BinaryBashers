package BinaryBashers.Enemies;

import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner<T extends Enemy> extends ActiveRenderer
{
    private Class<T> enemyType;
    private Constructor<T> enemyConstructor;

    private static EnemySpawner instance;

    public static EnemySpawner getInstance()
    {
        return instance;
    }

    private List<T> enemies;
    private float spawnInterval = 30f;
    private float spawnTimer;
    private Random random;

    public EnemySpawner(Class<T> enemyType)
    {
        this.enemyType = enemyType;
        this.enemies = new ArrayList<>();
        this.random = new Random();
        spawnTimer = Math.max(spawnInterval - 5, 0);
    }

    @Override
    public void awake()
    {
        try
        {
            enemyConstructor = enemyType.getDeclaredConstructor(Integer.class, Vector2.class);
        } catch (NoSuchMethodException e)
        {
            if (DialogBoxManager.getInstance() == null)
            {
                GameObject dialogManager = new GameObject("DialogBoxManager");
                var dial = new DialogBoxManager();
                dialogManager.addComponent(dial);
                dialogManager.transform.setPosition(Painter.renderCenter);
                owner.getScene().addObject(dialogManager);
                dial.awake();
            }

            System.out.println("Valid Enemy constructor not found!");
            DialogBoxManager.getInstance()
                    .enqueue("WARNING", "Enemy constructor not valid!\n" + enemyType.getName(),
                            40);
        }
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
        if (enemyConstructor == null)
            return;

        if (enemies.size() >= 3)
        {
            System.out.println("Already 3 enemies exist, cant add more");
            return;
        }

        int randomId = random.nextInt(3);
        Vector2 enemyPos = getNewEnemyPosition();

        T newEnemy = null;
        try
        {
            newEnemy = enemyConstructor.newInstance(randomId, enemyPos);
        } catch (InstantiationException
                 | InvocationTargetException
                 | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        newEnemy.startAnimation();
        enemies.add(newEnemy);
        newEnemy.spawner = this;

        System.out.println("Enemy spawned with ID: " + randomId + ". Total enemies: " + enemies.size());
    }

    private Vector2 getNewEnemyPosition()
    {
        int size = enemies.size();
        if (size == 1)
        {
            return Painter.renderCenter.subtract(new Vector2(Painter.renderCenter.x / 2, 0));
        } else if (size == 0)
        {
            return Painter.renderCenter;
        } else if (size == 2)
        {
            return Painter.renderCenter.add(new Vector2(Painter.renderCenter.x / 2, 0));
        } else
        {
            throw new IllegalStateException("Invalid Position");
        }
    }

    public void checkAndKillEnemies(String input)
    {
        for (var e : enemies)
        {
            if (e.compairInput(input))
            {
                e.death();
            }
        }
    }

    public void killEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
        System.out.println("Enemy removed. Total enemies: " + enemies.size());
    }

    public boolean hasEnemies()
    {
        return !enemies.isEmpty();
    }

    public List<T> getEnemies()
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
        for (var e : enemies)
            e.getSprite().onColorPalleteChange(colorPallet);
    }
}
