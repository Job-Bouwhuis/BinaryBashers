package BinaryBashers;

import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

public class App extends Application
{
    public App(boolean fullscreen) {super(fullscreen);}
    public static void main(String[] args)
    {
        //SaxionApp.startGameLoop(new App(), 1280, 720, 1);
        SaxionApp.startGameLoop(new App(true), 1920, 1080, 1);
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
            //backgroundSprite = SpritePalletChanger.changePallet(backgroundSprite, new ColorPallet(new Sprite("resources/colorPallets/main.png"), new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));

            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0, 0);
            GameObject backgroundObject = new GameObject("background");
            backgroundObject.addComponent(spriteRenderer);
            scene.addObject(backgroundObject);

            EnemySprite enemySprite = new EnemySprite();
            enemySprite.setSpriteId(SaxionApp.getRandomValueBetween(0, 2));
            //            enemySprite.startEnteringAnimation();
            GameObject enemyObject = new GameObject("TestEnemySprite");
            enemyObject.addComponent(enemySprite);
            enemyObject.transform.setPosition(new Vector2(200, 200));
            scene.addObject(enemyObject);
        });

        loadScene("testScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
