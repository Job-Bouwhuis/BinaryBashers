package BinaryBashers;

import BinaryBashers.Utils.Util;
import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

public class App extends Application
{
    public static void main(String[] args)
    {
        SaxionApp.startGameLoop(new App(), 1280, 720, 1);
    }

    @Override
    public void createScenes()
    {
//        createScene("testScene", scene -> {
//            Sprite sprite = Sprite.square(10, 10, Color.white);
//
//            GameObject obj = new GameObject("obj1");
//            obj.transform.getPosition().set(new Vector2(200, 300));
//            var ir = new InputRenderer(40);
//            ir.onEnterKeyPressed.add(renderer -> renderer.owner.destroy());
//            obj.addComponent(ir);
//            scene.addObject(obj);
//
//            GameObject button = new GameObject("button");
//            button.transform.getPosition().set(new Vector2(200, 100));
//            button.addComponent(new Button(Sprite.square(30, 20, Color.white)));
//
//            scene.addObject(button);
//        });

        createScene("levelScene", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");
            var spriteRenderer = new SpriteRenderer(backgroundSprite);
            spriteRenderer.origin = new Vector2(0,0);
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
        loadScene("levelScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
