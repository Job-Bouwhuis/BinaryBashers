package BinaryBashers.Levels;

import BinaryBashers.BackToMainMenuComponent;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;

public class LevelSelectScene
{
    public static void createScene(Application app)
    {
        app.createScene("LevelSelect", scene ->
        {
            var obj = scene.createObject("background");
            obj.addComponent(new SpriteRenderer(new Sprite("resources/sprites/levelSelect/SelectBackground.png"))).transform.setPosition(Painter.renderCenter);

            // lines
            var line1to2 = scene.createObject("line1to2");
            line1to2.transform.setPosition(new Vector2(148, 210));
            line1to2.addComponent(new SpriteRenderer(new Sprite("resources/sprites/levelSelect/paths/Level2Path.png")));

            var line2to3 = scene.createObject("line2to3");
            line2to3.transform.setPosition(new Vector2(365, 177));
            line2to3.addComponent(new SpriteRenderer(new Sprite("resources/sprites/levelSelect/paths/Level3Path.png")));

            var line3to4 = scene.createObject("line3to4");
            line3to4.transform.setPosition(new Vector2(497, 213));
            line3to4.addComponent(new SpriteRenderer(new Sprite("resources/sprites/levelSelect/paths/Level4Path.png")));

            // level buttons
            var level1 = scene.createObject("level1Button");
            level1.transform.setPosition(new Vector2(35, 235));
            var l1button = new Button(new Sprite("resources/sprites/levelSelect/levelButtons/Level1.png"));
            l1button.origin = new Vector2(.5f, .5f);
            l1button.text = new DefaultTextProvider("");
            level1.addComponent(l1button);
            l1button.onClick.add(button -> {
                app.loadScene("DecimalLevel");
            });

            var level2 = scene.createObject("level2Button");
            level2.transform.setPosition(new Vector2(248, 245));
            var l2button = new Button(new Sprite("resources/sprites/levelSelect/levelButtons/Level2.png"));
            l2button.origin = new Vector2(.5f, .5f);
            l2button.text = new DefaultTextProvider("");
            level2.addComponent(l2button);
            l2button.onClick.add(button -> {
                app.loadScene("BinaryLevel");
            });

            var level3 = scene.createObject("level3Button");
            level3.transform.setPosition(new Vector2(480, 133));
            var l3button = new Button(new Sprite("resources/sprites/levelSelect/levelButtons/Level3.png"));
            l3button.origin = new Vector2(.5f, .5f);
            l3button.text = new DefaultTextProvider("");
            level3.addComponent(l3button);
            l3button.onClick.add(button -> {
                app.loadScene("HexLevel");
            });

            var level4 = scene.createObject("level4Button");
            level4.transform.setPosition(new Vector2(508, 293));
            var l4button = new Button(new Sprite("resources/sprites/levelSelect/levelButtons/Level4.png"));
            l4button.origin = new Vector2(.5f, .5f);
            l4button.text = new DefaultTextProvider("");
            level4.addComponent(l4button);
            l4button.onClick.add(button -> {
//                DialogBoxManager.getInstance().enqueue("Attention!", "The infinite level is due for sprint 3, stay tuned for infinite madness!\n\n" +
//                        "Originally it was planned for this sprint, however we did not manage to do this in time due to other tasks required for this one taking longer than expected", dialog -> {});

                app.loadScene("EndlessLevel");
            });

            scene.createObject("gameCloser").addComponent(new BackToMainMenuComponent());

            //scene.setScenePallet(new ColorPallet(new Sprite("resources\\colorPallets\\midnight-ablaze.png")));
        });
    }
}