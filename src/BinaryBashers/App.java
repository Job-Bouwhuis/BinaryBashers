package BinaryBashers;

import BinaryBashers.Levels.DecimalToBinaryScene;
import BinaryBashers.Levels.HexToDecimalScene;
import BinaryBashers.Levels.LevelSelectScene;
import dev.WinterRose.SaxionEngine.*;
import BinaryBashers.Levels.BinaryToDecimalScene;

public class App extends Application
{
    public App(boolean fullscreen)
    {
        super(fullscreen);
    }

    public static void main(String[] args)
    {
        new App(false).run(1280, 720, 60);
    }

    @Override
    public void createScenes()
    {
        new BinaryToDecimalScene().createScene(this);
        new HexToDecimalScene().createScene(this);
        new DecimalToBinaryScene().createScene(this);
        LevelSelectScene.createScene(this);
        loadScene("LevelSelect", false);
    }

    @Override
    public void createPrefabs()
    {

    }
}
