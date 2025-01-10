package BinaryBashers;

import BinaryBashers.Levels.*;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;

public class App extends Application
{
    public App(boolean fullscreen)
    {
        super(fullscreen);
    }

    public static void main(String[] args)
    {
        new App(false).run(1280, 720, 50);
    }

    @Override
    public void createScenes()
    {
        new BinaryToDecimalScene().createScene(this);
        new HexToDecimalScene().createScene(this);
        new DecimalToBinaryScene().createScene(this);
        new EndlessLevelScene().createScene(this);
        LevelSelectScene.createScene(this);
        TitlescreenScene.createScene(this);
        loadScene("TitleScreen", false);
    }

    @Override
    public void createPrefabs()
    {

    }
}
