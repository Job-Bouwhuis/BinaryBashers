package BinaryBashers.Levels;

import dev.WinterRose.SaxionEngine.*;

public class TitlescreenScene
{
    public static void createScene(Application app)
    {
        app.createScene("TitleScreen", scene -> {
            var background = scene.createObject("background");
            background.addComponent(new SpriteRenderer(new Sprite("resources/sprites/titleScreen/Titlescreen.png")));
            background.transform.setPosition(Painter.renderCenter);

            var text = scene.createObject("text");
            text.addComponent(new SpriteRenderer(new Sprite("resources/sprites/titleScreen/Logo.png")));
            text.transform.setPosition(new Vector2(190, 100));
            text.transform.setScale(new Vector2(1f, 1f));

            // make 2 text, one black for shadow thats a little bigger
            // one for actual text "press 1 to play, 0 to quit"
        });
    }
}
