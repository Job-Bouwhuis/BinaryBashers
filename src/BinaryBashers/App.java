package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

import java.awt.*;

public class App extends Application
{
    public static void main(String[] args)
    {
        SaxionApp.startGameLoop(new App(), 1920, 1080, 1);
    }

    @Override
    public void createScenes()
    {
        createScene("jobTestScene", scene -> {
            GameObject parent = new GameObject("parent");
            parent.addComponent(new SpriteRenderer(Sprite.square(20, 20, Color.white)));
            parent.addComponent(new MoveTest());
            scene.addObject(parent);

            GameObject child = new GameObject("child");
            child.addComponent(new SpriteRenderer(Sprite.square(15, 15, Color.blue)));
            child.transform.setPosition(new Vector2(0, 25));
            child.transform.setParent(parent.transform);
            scene.addObject(child);
        });

        loadScene("jobTestScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
