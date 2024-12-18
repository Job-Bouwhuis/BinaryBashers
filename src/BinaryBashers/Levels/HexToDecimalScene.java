package BinaryBashers.Levels;

import BinaryBashers.Enemies.EnemySpawner;
import BinaryBashers.Enemies.HexEnemy;
import BinaryBashers.Player;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

public class HexToDecimalScene
{
    public void createScene(Application app)
    {
        app.createScene("HexLevel", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");

            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0, 0);
            GameObject backgroundObject = new GameObject("background");
            backgroundObject.addComponent(spriteRenderer);
            scene.addObject(backgroundObject);

            EnemySpawner<?> enemySpawner = new EnemySpawner<>(HexEnemy.class,true);
            GameObject spawner = new GameObject("spawner");
            Sprite timerSprite = new Sprite("resources/sprites/ui/timer/Timer1.png");
            Timer enemySpawnTimer = new Timer(5, true, true, 1);
            Timer playerDamageTimer = new Timer(10, true, true, 1);
            Player player = new Player(3, enemySpawner);
            GameObject playerObj = new GameObject("player");
            playerObj.addComponent(player);
            playerObj.addComponent(playerDamageTimer);

            player.transform.setPosition(new Vector2(Painter.renderWidth - timerSprite.getwidth(), Painter.renderHeight - timerSprite.getHeight()));
            spawner.addComponent(enemySpawner);
            spawner.addComponent(enemySpawnTimer);
            scene.addObject(spawner);
            scene.addObject(playerObj);
            GameObject inputField = new GameObject("inputRenderer");
            InputRenderer inputRenderer = new InputRenderer(1);
            enemySpawner.inputRenderer = inputRenderer;
            inputRenderer.onEnterKeyPressed.add(inputRenderer1 -> {
                enemySpawner.checkAndKillEnemies(inputRenderer1.getInputAsString());
                inputRenderer1.inputText.clear();
            });
            inputRenderer.acceptedCharacters = new Character[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A','B','C','D','E','F'};
            inputField.addComponent(inputRenderer);
            inputField.transform.setPosition(new Vector2(Painter.renderCenter).add(new Vector2(0, (float) Painter.renderHeight / 2)));
            scene.addObject(inputField);
            scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/midnight-ablaze.png")));
        });
    }
}
