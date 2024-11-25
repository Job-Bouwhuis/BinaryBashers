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
        var t = new InputRenderer("101");
        t.onCorrectTextComplete.add(ir -> System.out.println("correct text!"));
        t.acceptedCharacters = new Character[]{ '1', '0' };
        t.correctCharacterColor = Color.green;
        t.wrongCharacterColor = Color.green;
        t.placeholderCharacterColor = Color.lightGray;
        t.placeholderChar = ' ';

        GameObject obj3 = new GameObject("obj3");
        obj3.addComponent(new TextRenderer("5"));
        obj3.transform.getPosition().set(new Vector2(0, -20));

        obj2.addComponent(t);
        obj2.transform.setParent(obj.transform);
        obj3.transform.setParent(obj.transform);
        scene.addObject(obj2);
        scene.addObject(obj3);

        return scene;
    }

    @Override
    public Scene[] preLoadOtherScenes()
    {
        // no extra scenes to preload
        return new Scene[0];
    }
}
