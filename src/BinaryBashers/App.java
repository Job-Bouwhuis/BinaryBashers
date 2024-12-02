package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.Button;
import nl.saxion.app.SaxionApp;

import java.awt.*;

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
            Sprite sprite = Sprite.square(10, 10, Color.white);

            GameObject text = new GameObject("textstuff");
            var inrend = new TextRenderer("This is some awesome text \ndid you know that? \nNo? \nWell you should");
//            inrend.getTextProvider().getWords().get(2).fontType = FontType.Bold;
//            inrend.getTextProvider().getWords().get(6).setColor(Color.red);
//            inrend.getTextProvider().getWords().get(8).getCharacters()[2].color = Color.magenta;
            text.addComponent(inrend);
            scene.addObject(text);

            GameObject button = new GameObject("button");
            button.transform.setPosition(new Vector2(200, 100));
            var butt = new Button(Sprite.square(30, 20, Color.white));
            button.addComponent(butt);
            butt.onClick.add(b -> closeGame());

            scene.addObject(button);
        });

        createScene("levelScene", scene -> {
            Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");
            //backgroundSprite = SpritePalletChanger.changePallet(backgroundSprite, new ColorPallet(new Sprite("resources/colorPallets/main.png"), new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));

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

            GameObject in = new GameObject("input");
            in.transform.setPosition(new Vector2(200, 200));

            scene.addObject(in);
        });
        loadScene("testScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
