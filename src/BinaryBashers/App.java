package BinaryBashers;

import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.*;
import BinaryBashers.Enemies.BinaryEnemy;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import BinaryBashers.Enemies.EnemySpawner;
import nl.saxion.app.SaxionApp;

public class App extends Application
{
    public App(boolean fullscreen)
    {
        super(fullscreen);
    }

    public static void main(String[] args)
    {
        new App(false).run(1280, 720);
    }

    @Override
    public void createScenes()
    {
        createScene("testScene", scene -> {
            GameObject button = new GameObject("button");
            button.transform.setPosition(Painter.renderCenter);
            var boxManager = new DialogBoxManager();
            button.addComponent(boxManager);

            scene.addObject(button);
        });

        createScene("levelScene", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");

            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0, 0);
            GameObject backgroundObject = new GameObject("background");
            backgroundObject.addComponent(spriteRenderer);
            scene.addObject(backgroundObject);

            EnemySprite enemySprite = new EnemySprite();
            enemySprite.setSpriteId(SaxionApp.getRandomValueBetween(0, 2));
            enemySprite.startEnteringAnimation();
            GameObject enemyObject = new GameObject("TestEnemySprite");
            enemyObject.addComponent(enemySprite);
            enemyObject.transform.setPosition(new Vector2(200, 200));
            scene.addObject(enemyObject);

            Sprite[] timerSprites = {
                    new Sprite("resources/sprites/ui/timer/Timer1.png"),
                    new Sprite("resources/sprites/ui/timer/Timer2.png"),
                    new Sprite("resources/sprites/ui/timer/Timer3.png"),
                    new Sprite("resources/sprites/ui/timer/Timer4.png")
            };
            AnimatedSpriteRenderer timerSprite = new AnimatedSpriteRenderer(timerSprites, 0.5f, true);
            GameObject timerObject = new GameObject("TimerObject");
            timerObject.addComponent(timerSprite);
            timerObject.transform.setPosition(new Vector2(500, 200
            ));

            scene.addObject(timerObject);
            scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));
        });


        createScene("spawnerTest", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");

            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0, 0);
            GameObject backgroundObject = new GameObject("background");
            backgroundObject.addComponent(spriteRenderer);
            scene.addObject(backgroundObject);
            EnemySpawner enemySpawner = new EnemySpawner(BinaryEnemy.class);
            GameObject spawner = new GameObject("spawner");
            Sprite timerSprite = new Sprite("resources/sprites/ui/timer/Timer1.png");
            Timer timer = new Timer(5, true, true, 1);

            spawner.transform.setPosition(new Vector2(Painter.renderWidth - timerSprite.getwidth(), Painter.renderHeight - timerSprite.getHeight()));
            spawner.addComponent(enemySpawner);
            spawner.addComponent(timer);

            scene.addObject(spawner);

            GameObject inputField = new GameObject("inputRenderer");
            InputRenderer inputRenderer = new InputRenderer(4);
            inputRenderer.onEnterKeyPressed.add(inputRenderer1 -> {
                enemySpawner.checkAndKillEnemies(inputRenderer1.getInputAsString());
                inputRenderer1.inputText.clear();
            });
            inputRenderer.acceptedCharacters = new Character[]{'0', '1'};
            inputField.addComponent(inputRenderer);
            inputField.transform.setPosition(new Vector2(Painter.renderCenter).add(new Vector2(0, (float) Painter.renderHeight / 2)));
            scene.addObject(inputField);
            scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));
        });

        loadScene("spawnerTest");
    }

    @Override
    public void createPrefabs()
    {

    }
}
