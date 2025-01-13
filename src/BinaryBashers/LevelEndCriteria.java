package BinaryBashers;

import BinaryBashers.Enemies.EnemySpawner;
import dev.WinterRose.SaxionEngine.Behavior;
import BinaryBashers.Levels.LevelEndScene;

public class LevelEndCriteria extends Behavior
{
    private EnemySpawner spawner;
    private final int enemiesToKill;
    private final int level;
    private int enemiesKilled = 0;
    boolean didMyJob = false;


    public LevelEndCriteria(int enemiesToKill, int level)
    {
        this.enemiesToKill = enemiesToKill;
        this.level = level;


    }

    @Override
    public void awake()
    {
        spawner = EnemySpawner.getInstance();
        spawner.onEnemyKilled.add(enemy -> enemiesKilled++);
    }

    @Override
    public void update()
    {
        if(enemiesKilled >= enemiesToKill && !didMyJob)
        {
            didMyJob = true;
            LevelEndScene.setNextAndLoad(level, owner.getScene().getScenePallet());
        }
    }
}
