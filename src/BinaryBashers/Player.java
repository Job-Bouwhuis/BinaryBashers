package BinaryBashers;

import BinaryBashers.Enemies.Enemy;
import BinaryBashers.Enemies.EnemySpawner;
import BinaryBashers.Enemies.ScoreManager;
import BinaryBashers.UI.HealthImage;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;
import dev.WinterRose.SaxionEngine.DialogBoxes.ConfirmationDialogBox;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;

import java.awt.*;
import java.util.ArrayList;

public class Player extends Renderer
{

    private int health = 3;
    private final EnemySpawner spawner;

    private final ScoreManager scoreManager;

    private Timer damageTimer;

    // the 3 hearts on the UI for health. currently not made to easily scale, perhaps in the future.
    private HealthImage heart1;
    private HealthImage heart2;
    private HealthImage heart3;

    private Sound damageSound = new Sound("resources/audio/takeDamage.wav");

    public Player(int health, EnemySpawner<?> spawner)
    {
        scoreManager = ScoreManager.getInstance();
        this.health = health;

        this.spawner = spawner;
        spawner.onEnemyCountChanged.add(newEnemyCount -> {

            if (newEnemyCount == 0) damageTimer.stop();
            else if (!damageTimer.isRunning()) damageTimer.restart();

            damageTimer.setSpeedMultiplier(switch (newEnemyCount)
            {
                case 1 -> 1.2f;
                case 2 -> 1.4f;
                case 3 -> 1.6f;
                default -> 1;
            });
        });
    }

    @Override
    public void awake()
    {
        damageTimer = owner.getComponent(Timer.class);
        damageTimer.onTimeAction.add(t -> takeDamage(1));
        damageTimer.stop();

        Sprite heartImage = new HealthImage(new Vector2(), new Vector2(1)).heartImage;
        final float heartScale = .8f;
        Vector2 effectiveScale = new Vector2(heartImage.getWidth() * heartScale, heartImage.getHeight() * heartScale);
        heart1 = new HealthImage(new Vector2(0 + effectiveScale.x, Painter.renderHeight - effectiveScale.y), new Vector2(heartScale));

        heart2 = new HealthImage(new Vector2(heart1.getPosition().x + effectiveScale.x + 3, heart1.getPosition().y), new Vector2(heartScale));

        heart3 = new HealthImage(new Vector2(heart2.getPosition().x + effectiveScale.x + 3, heart2.getPosition().y), new Vector2(heartScale));

        if (heart1 != null) heart1.heartImage = SpritePalletChanger.changePallet(heart1.heartImage, owner.getScene().getScenePallet());
        if (heart2 != null) heart2.heartImage = SpritePalletChanger.changePallet(heart2.heartImage, owner.getScene().getScenePallet());
        if (heart3 != null) heart3.heartImage = SpritePalletChanger.changePallet(heart3.heartImage, owner.getScene().getScenePallet());

        spawner.onEnemyKilled.add(swnr -> damageTimer.restart());
    }

    public int getHealth()
    {
        return health;
    }

    public void takeDamage(int damage)
    {
        if (spawner.hasEnemies())
        {
            spawner.startRandomEnemyDamageAnimation();
            health -= damage;
            damageSound.play();
            System.out.println("Player takes " + damage + " damage. Remaining health: " + health);
            if (health <= 0) death();
        }

        if (health == 3)
        {
            heart1.setAlive(true);
            heart2.setAlive(true);
            heart3.setAlive(true);
        }
        if (health == 2)
        {
            heart1.setAlive(true);
            heart2.setAlive(true);
            heart3.setAlive(false);
        }
        if (health == 1)
        {

            heart1.setAlive(true);
            heart2.setAlive(false);
            heart3.setAlive(false);
        }
        if (health == 0)
        {
            heart1.setAlive(false);
            heart2.setAlive(false);
            heart3.setAlive(false);
        }
    }

    public void death()
    {
        damageTimer.stop();

        StringBuilder remainingEnemyAnswers = new StringBuilder();
        ArrayList<Enemy> es = EnemySpawner.getInstance().getEnemies();
        for (Enemy e : es)
            remainingEnemyAnswers.append("%s = %s".formatted(e.problem(), e.answer()) + "\n");


        var box = new ConfirmationDialogBox("DEDE", "You died. Score: " + scoreManager.getCurrentScore() +"\n\n" + remainingEnemyAnswers, confirmationDialogBox -> {
            confirmationDialogBox.setPlaySounds(false);
            DialogBoxManager.getInstance().clearAll(true);
            Application.current().loadScene("LevelSelect");
        });

        box.setShowCancelButton(false);
        box.getConfirmButton().text = new DefaultTextProvider("Return");
        box.getConfirmButton().text.setColor(Color.black);

        DialogBoxManager.getInstance()
                .enqueue(box);
    }

    @Override
    public void render(Painter painter)
    {
        heart1.paint(painter);
        heart2.paint(painter);
        heart3.paint(painter);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        if (heart1 != null) heart1.heartImage = SpritePalletChanger.changePallet(heart1.heartImage, colorPallet);
        if (heart2 != null) heart2.heartImage = SpritePalletChanger.changePallet(heart2.heartImage, colorPallet);
        if (heart3 != null) heart3.heartImage = SpritePalletChanger.changePallet(heart3.heartImage, colorPallet);
    }
}
