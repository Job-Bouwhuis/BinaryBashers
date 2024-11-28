package BinaryBashers;

import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import BinaryBashers.Utils.Util;
import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

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
            parent.transform.setPosition(Util.screenCenter());
//            var boxRenderer = new BoxRenderer(new Vector2(250, 160));
//            boxRenderer.backgroundColor = Color.pink;
//            parent.addComponent(boxRenderer);
            var dialogManager = new DialogBoxManager();
            parent.addComponent(dialogManager);
            scene.addObject(parent);
        });

        loadScene("jobTestScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
