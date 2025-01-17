package BinaryBashers;

import BinaryBashers.Enemies.EnemySpawner;
import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Behavior;
import BinaryBashers.Levels.LevelEndScene;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

public class LevelEndCriteria extends Behavior
{
    private EnemySpawner spawner;
    private final int enemiesToKill;
    private final int level;
    private int enemiesKilled = 0;
    boolean didMyJob = false;
    boolean didMyColorPalletSwappingJob = false;
    boolean altColorEnabled;
    private int enemiesUntilPalletSwitch;
    ColorPallet altPallet;

    public LevelEndCriteria(int enemiesToKill, int level)
    {
        this.enemiesToKill = enemiesToKill;
        this.level = level;
    }

    public LevelEndCriteria(int enemiesToKill, int level, int enemiesUntilPalletSwitch, ColorPallet altPallet)
    {
        this.enemiesToKill = enemiesToKill;
        this.level = level;
        this.enemiesUntilPalletSwitch = enemiesUntilPalletSwitch;
        this.altPallet = altPallet;
        altColorEnabled = true;
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
        if (altColorEnabled && enemiesKilled >= enemiesUntilPalletSwitch && !didMyColorPalletSwappingJob)
        {
            didMyColorPalletSwappingJob = true;
            Application.current().getActiveScene().setScenePallet(altPallet);
            owner.getScene().setScenePalletUnAnnounced(new ColorPallet(new ColorPallet(), altPallet));
        }
    }
}
