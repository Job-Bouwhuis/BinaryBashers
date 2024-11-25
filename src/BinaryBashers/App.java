package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

import java.awt.*;

public class App extends Application
{
    public static void main(String[] args)
    {
        SaxionApp.startGameLoop(new App(), 1280, 720, 1);
    }

    @Override
    public Scene createScene()
    {
        Sprite sprite = Sprite.square(10, 10, Color.RED);

        Scene scene = new Scene("test scene");
        GameObject obj = new GameObject("obj1");
        obj.transform.getPosition().set(new Vector2(100, 100));
        obj.addComponent(new SpriteRenderer(sprite));
        obj.addComponent(new MoveTest());
        scene.addObject(obj);

        GameObject obj2 = new GameObject("obj2");
        obj2.transform.getPosition().set(new Vector2(0, 5));
        var t = new InputRenderer("1011");
        t.onCorrectTextComplete = ir -> System.out.println("correct text!");
        t.acceptedCharacters = new Character[] {'1', '0'};
        t.correctCharacterColor = Color.green;
        t.wrongCharacterColor = Color.green;
        t.placeholderCharacterColor = Color.darkGray;
        obj2.addComponent(t);
        obj2.transform.setParent(obj.transform);
        scene.addObject(obj2);
        return scene;
    }

    @Override
    public Scene[] preLoadOtherScenes()
    {
        // no extra scenes to preload
        return new Scene[0];
    }
}
