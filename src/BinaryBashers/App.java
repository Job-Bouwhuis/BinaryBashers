package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.Button;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;
import nl.saxion.app.SaxionApp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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
            ColorPallet pallet = new ColorPallet(
                    new Sprite("resources\\colorPallets\\main.png"),
                    new Sprite("resources\\colorPallets\\midnightAblaze\\midnight-ablaze.png"));

            Sprite original = new Sprite("resources\\testing\\Decimal Demon.png");
            Sprite palletChanged = SpritePalletChanger.changePallet(original, pallet);

            File file = new File("test.png");
            try
            {
                ImageIO.write(palletChanged.getImageRaw(), "png", file);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            GameObject obj = new GameObject("obj1");
            obj.transform.getPosition().set(new Vector2(200, 300));


            scene.addObject(obj);
        });

        loadScene("testScene");
    }

    @Override
    public void createPrefabs()
    {

    }
}
