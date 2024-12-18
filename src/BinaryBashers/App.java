package BinaryBashers;

import BinaryBashers.Enemies.EnemySprite;
import BinaryBashers.Levels.DecimalToBinaryScene;
import BinaryBashers.Levels.HexToDecimalScene;
import BinaryBashers.Levels.LevelSelectScene;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import BinaryBashers.Levels.BinaryToDecimalScene;
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
                    new Sprite("resources/sprites/ui/timer/Timer4.png"),
                    new Sprite("resources/sprites/ui/timer/Timer5.png"),
                    new Sprite("resources/sprites/ui/timer/Timer6.png"),
                    new Sprite("resources/sprites/ui/timer/Timer7.png"),
                    new Sprite("resources/sprites/ui/timer/Timer8.png"),
            };
            AnimatedSpriteRenderer timerSprite = new AnimatedSpriteRenderer(timerSprites, 0.5f, true);
            GameObject timerObject = new GameObject("TimerObject");
            timerObject.addComponent(timerSprite);
            timerObject.transform.setPosition(new Vector2(500, 200
            ));

            scene.addObject(timerObject);
            scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));
        });
        new BinaryToDecimalScene().createScene(this);
        new HexToDecimalScene().createScene(this);
        new DecimalToBinaryScene().createScene(this);
        LevelSelectScene.createScene(this);
        loadScene("LevelSelect");

    }

    @Override
    public void createPrefabs()
    {

    }
}
