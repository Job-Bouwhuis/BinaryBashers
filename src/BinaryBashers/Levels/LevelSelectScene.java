package BinaryBashers.Levels;

import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.Sprite;
import dev.WinterRose.SaxionEngine.SpriteRenderer;

public class LevelSelectScene
{
    public static void createScene(Application app)
    {
        app.createScene("LevelSelect", scene -> {
            scene.createObject("background").addComponent(new SpriteRenderer(new Sprite("resources/sprites/levelSelect/SelectBackground.png"))).transform.setPosition(Painter.renderCenter);
            //scene.setScenePallet(new ColorPallet(new Sprite("resources\\colorPallets\\yellow.png")));
        });
    }
}
