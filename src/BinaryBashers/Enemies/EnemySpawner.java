package BinaryBashers.Enemies;

import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.awt.*;
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
        spawnTimer = 5;
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
        spawnTimer -= Time.deltaTime;
        if (spawnTimer <= 0)
        {
            spawnEnemy();
            spawnTimer = spawnInterval;
        }

        for (int i = 0; i < enemies.size(); i++)
        {
            var e = enemies.get(i);
            e.update();
        }
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
        if(owner.getScene().getScenePallet() != null)
            newEnemy.getSprite().onColorPalleteChange(owner.getScene().getScenePallet());

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
        for (int i = 0; i < enemies.size(); i++)
        {
            var e = enemies.get(i);
            if (e.compairInput(input))
            {
                e.kill();
            }
        }
    }

    public void killEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
        System.out.println("Enemy removed. Total enemies: " + enemies.size());
        if(enemies.isEmpty() && spawnTimer > 5)
            spawnTimer = 5; // no enemies, set timer to 5 seconds until a new one spawns to keep gameflow going
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
        painter.drawScaledText("Time until next enemy: " + Math.round(spawnTimer) + "s", new Vector2(2), new Vector2(0.9f), new Vector2(), Color.white, FontType.Bold);

        for (int i = 0; i < enemies.size(); i++)
        {
            var e = enemies.get(i);
            e.render(painter);
        }
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        for (int i = 0; i < enemies.size(); i++)
        {
            var e = enemies.get(i);
            e.getSprite().onColorPalleteChange(colorPallet);
        }
    }
}
