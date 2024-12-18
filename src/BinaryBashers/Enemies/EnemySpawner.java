package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner<T extends Enemy> extends ActiveRenderer
{
    public Action<Integer> onEnemyCountChanged = new Action<>();
    private Class<T> enemyType;
    private Constructor<T> enemyConstructor;
    private ScoreManager scoreManager = ScoreManager.getInstance();
    public InputRenderer inputRenderer;

    private static EnemySpawner instance;

    public static EnemySpawner getInstance()
    {
        return instance;
    }

    private List<T> enemies;
    private float spawnInterval = 30f;
    private float spawnTimer;
    private Random random;
    private Timer timer;
    private DifficultyGenerator difficultyGenerator = new DifficultyGenerator();
    private Boolean fromDecimal;

    public EnemySpawner(Class<T> enemyType, Boolean fromDecimal)
    {
        this.enemyType = enemyType;
        this.enemies = new ArrayList<>();
        this.random = new Random();
        spawnTimer = 5;
        this.fromDecimal = fromDecimal;
    }

    @Override
    public void awake()
    {
        timer = owner.getComponent(Timer.class);
        timer.onTimeAction.add(t -> timeElapsed(t));
        timer.setSprites(new Sprite[0]);
        try
        {
            enemyConstructor = enemyType.getDeclaredConstructor(Integer.class, Vector2.class, Integer.class, Boolean.class);
        }
        catch (NoSuchMethodException e)
        {
            System.out.println("Valid Enemy constructor not found!");
            DialogBoxManager.getInstance()
                    .enqueue("WARNING", "Enemy constructor not valid!\n" + enemyType.getName(), 40);
        }
    }

    //4 sec is 1 frame per seconde. aantal sec delen door frame voor animatie
    @Override
    public void update()
    {
        //        System.out.println(timer.getSpeedMultiplier());
        for (int i = 0; i < enemies.size(); i++)
        {
            var e = enemies.get(i);
            e.update();
        }
    }

    private void timeElapsed(Timer timer)
    {
        spawnEnemy();
        if (enemies.size() > 0)
        {
            timer.setMaxTime(spawnInterval);
        }

    }

    // Spawns an enemy with a random ID
    public void spawnEnemy()
    {
        if (enemyConstructor == null) return;

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
            newEnemy = enemyConstructor.newInstance(randomId, enemyPos, difficultyGenerator.getDifficultyNumber(scoreManager.getCurrentScore()), fromDecimal);
            if (newEnemy.getInputLength() > inputRenderer.characterMax)
            {
                inputRenderer.characterMax = newEnemy.getInputLength();
            }
        }
        catch (InstantiationException | InvocationTargetException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        newEnemy.startAnimation();
        enemies.add(newEnemy);
        newEnemy.spawner = this;
        if (owner.getScene().getScenePallet() != null)
            newEnemy.getSprite().onColorPalleteChange(owner.getScene().getScenePallet());

        System.out.println("Enemy spawned with ID: " + randomId + ". Total enemies: " + enemies.size());

        onEnemyCountChanged.invoke(enemies.size());
    }

    private Vector2 getNewEnemyPosition()
    {
        int size = enemies.size();
        if (size == 1)
        {
            return Painter.renderCenter.subtract(new Vector2(Painter.renderCenter.x / 2, 0));
        }
        else if (size == 0)
        {
            return Painter.renderCenter;
        }
        else if (size == 2)
        {
            return Painter.renderCenter.add(new Vector2(Painter.renderCenter.x / 2, 0));
        }
        else
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
                scoreManager.addPoints(1);
            }
        }
    }

    public void killEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
        onEnemyCountChanged.invoke(enemies.size());
        System.out.println("Enemy removed. Total enemies: " + enemies.size());
        if (enemies.isEmpty() && timer.getTimeLeft() > 5)
        {
            timer.skipTo(timer.getMaxTime() - 5); // no enemies, set timer to 5 seconds until a new one spawns to keep gameflow going
            timer.setSpeedMultiplier(1f);
        }
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
