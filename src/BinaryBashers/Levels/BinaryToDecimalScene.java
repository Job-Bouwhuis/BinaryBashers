package BinaryBashers.Levels;

import BinaryBashers.BackToMainMenuComponent;
import BinaryBashers.Enemies.BinaryEnemy;
import BinaryBashers.Enemies.EnemySpawner;
import BinaryBashers.LevelEndCriteria;
import BinaryBashers.Player;
import BinaryBashers.UI.ScoreOnScreen;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

public class BinaryToDecimalScene
{
    public void createScene(Application app)
    {
        app.createScene("BinaryLevel", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");

            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0, 0);
            GameObject backgroundObject = new GameObject("background");
            backgroundObject.addComponent(spriteRenderer);
            scene.addObject(backgroundObject);

            EnemySpawner<?> enemySpawner = new EnemySpawner<>(BinaryEnemy.class, false);
            GameObject spawner = new GameObject("spawner");
            Sprite timerSprite = new Sprite("resources/sprites/ui/timer/Timer1.png");
            Timer enemySpawnTimer = new Timer(3, true, true, 1);
            Timer playerDamageTimer = new Timer(20, true, true, 1);
            Player player = new Player(3, enemySpawner);
            GameObject playerObj = new GameObject("player");
            playerObj.addComponent(player);
            playerObj.addComponent(playerDamageTimer);

            player.transform.setPosition(new Vector2(Painter.renderWidth - timerSprite.getWidth(), Painter.renderHeight - timerSprite.getHeight()));
            spawner.addComponent(enemySpawner);
            spawner.addComponent(enemySpawnTimer);
            scene.addObject(spawner);
            scene.addObject(playerObj);
            GameObject inputField = new GameObject("inputRenderer");
            InputRenderer inputRenderer = new InputRenderer(4);
            enemySpawner.inputRenderer = inputRenderer;
            inputRenderer.onEnterKeyPressed.add(inputRenderer1 -> {
                enemySpawner.checkAndKillEnemies(inputRenderer1.getInputAsString());
                inputRenderer1.inputText.clear();
            });
            inputField.addComponent(inputRenderer);
            inputField.transform.setPosition(new Vector2(Painter.renderCenter).add(new Vector2(0, (float) Painter.renderHeight / 2)));
            scene.addObject(inputField);
            scene.createObject("backtolevelselect").addComponent(new BackToMainMenuComponent());
            scene.createObject("scoreText").addComponent(new ScoreOnScreen());
            scene.createObject("levelEndCriteria").addComponent(new LevelEndCriteria(18, 2, 10, new ColorPallet(new Sprite("resources/colorPallets/blue.png"))));
            scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/red.png")));
        });
    }
}
