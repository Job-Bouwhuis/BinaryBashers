package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.Button;
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
        createScene("testScene", scene -> {
            Sprite sprite = Sprite.square(10, 10, Color.white);

            GameObject obj = new GameObject("obj1");
            obj.transform.getPosition().set(new Vector2(200, 300));
            var ir = new InputRenderer(40);
            ir.onEnterKeyPressed.add(renderer -> renderer.owner.destroy());
            obj.addComponent(ir);
            scene.addObject(obj);

            GameObject button = new GameObject("button");
            button.transform.getPosition().set(new Vector2(200, 100));
            button.addComponent(new Button(Sprite.square(30, 20, Color.white)));

            scene.addObject(button);
        });

        loadScene("testScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
