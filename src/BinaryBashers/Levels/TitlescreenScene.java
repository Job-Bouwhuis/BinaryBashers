package BinaryBashers.Levels;

import BinaryBashers.TitleScreenManager;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.Word;

import java.util.ArrayList;
import java.util.Random;

public class TitlescreenScene
{
    public static void createScene(Application app)
    {
        app.createScene("TitleScreen", scene -> {
            var background = scene.createObject("background");
            background.addComponent(new SpriteRenderer(new Sprite("resources/sprites/titleScreen/Titlescreen.png")));
            background.transform.setPosition(Painter.renderCenter);
            background.addComponent(new TitleScreenManager());

            var titleText = scene.createObject("titleText");
            titleText.addComponent(new SpriteRenderer(new Sprite("resources/sprites/titleScreen/Logo.png")));
            titleText.transform.setPosition(new Vector2(190, 100));
            titleText.transform.setScale(new Vector2(1f, 1f));

            // make 2 titleText, one black for shadow thats a little bigger
            // one for actual titleText "press 1 to play, 0 to quit"

            DefaultTextProvider textProvider = new DefaultTextProvider("");

            String[] colorPallets = new String[] {
                    "resources/colorPallets/main.png",
                    "resources/colorPallets/midnight-ablaze.png",
                    "resources/colorPallets/Yellow.png",
                    "resources/colorPallets/blue.png"
            };

            ColorPallet pallet;
            String palletPath = colorPallets[new Random().nextInt(0, colorPallets.length - 1)];
            if(palletPath.contains("main"))
                pallet = new ColorPallet();
            else
                pallet = new ColorPallet(new Sprite(palletPath));

            background.getComponent(SpriteRenderer.class).onColorPalleteChange(pallet);
            titleText.getComponent(SpriteRenderer.class).onColorPalleteChange(pallet);

            ArrayList<Word> words = new ArrayList<>();
            words.add(new Word("Press ", textProvider));
            words.add(new Word("1 ", textProvider));
            words.addAll(Word.ParseWords("to play, ", textProvider));
            words.add(new Word(" ", textProvider));
            words.add(new Word("0 ", textProvider));
            words.addAll(Word.ParseWords("to quit.", textProvider));
            textProvider.setWords(words);

            for (Word word : words)
                word.setColor(pallet.getColorFromIndex(4));

            textProvider.getWords().get(1).setColor(pallet.getColorFromIndex(3));
            textProvider.getWords().get(1).fontType = FontType.Bold;

            textProvider.getWords().get(5).setColor(pallet.getColorFromIndex(3));
            textProvider.getWords().get(5).fontType = FontType.Bold;

            TextRenderer textRenderer = new TextRenderer();
            textRenderer.setText(textProvider);

            var startText = scene.createObject("startText");
            startText.addComponent(textRenderer);
            startText.transform.setPosition(Painter.renderCenter);
            startText.transform.setPosition(startText.transform.getPosition()
                    .subtract(new Vector2(108, 0))
                    .add(new Vector2(0, 10)));


            TextProvider authorsProvider = new DefaultTextProvider();
            authorsProvider.setDefaultColor(pallet.getColorFromIndex(4));

            ArrayList<Word> authorsWords = new ArrayList<>();
            authorsWords.addAll(Word.ParseWords("By Almar", authorsProvider));
            authorsWords.addAll(Word.ParseWords(", Leon", authorsProvider));
            authorsWords.addAll(Word.ParseWords(",\n Ruben", authorsProvider));
            authorsWords.addAll(Word.ParseWords(", and Job", authorsProvider));
            authorsWords.add(new Word(".", authorsProvider));

            TitlescreenScene.makeAuthorName(authorsWords.get(1), pallet);
            TitlescreenScene.makeAuthorName(authorsWords.get(3), pallet);
            TitlescreenScene.makeAuthorName(authorsWords.get(5), pallet);
            TitlescreenScene.makeAuthorName(authorsWords.get(8), pallet);

            authorsProvider.setWords(authorsWords);

            TextRenderer authorsRenderer = new TextRenderer("");
            authorsRenderer.setText(authorsProvider);

            var authors = scene.createObject("authors");
            authors.addComponent(authorsRenderer);
            authors.transform.setPosition(startText.transform.getPosition().add(new Vector2(0, 30)));
            authors.transform.setScale(new Vector2(1, 1));
        });
    }

    private static void makeAuthorName(Word name, ColorPallet pallet)
    {
        name.fontType = FontType.Bold;
        name.setColor(pallet.getColorFromIndex(3));
    }
}
