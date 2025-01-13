package BinaryBashers.Levels;

import BinaryBashers.Enemies.ScoreManager;
import dev.WinterRose.SaxionEngine.Button;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.DialogBoxes.ConfirmationDialogBox;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.DialogBoxes.TimedDialogBox;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;

import java.awt.*;

public class LevelEndScene
{
    private static int level;
    private static ColorPallet pallet;

    public static void setNextAndLoad(int level, ColorPallet pallet)
    {
        LevelEndScene.level = level;
        LevelEndScene.pallet = pallet;
        DialogBoxManager.getInstance().clearAll(true);
        Application.current().loadScene("levelEndScene");
    }

    public static void setNextAndNotLoad(int level, ColorPallet pallet)
    {
        LevelEndScene.level = level;
        LevelEndScene.pallet = pallet;
    }

    public static void createScene(Application app)
    {
        app.createScene("levelEndScene", scene -> createScene(scene));
    }

    private static void createScene(Scene scene)
    {
        ColorPallet basePallet = new ColorPallet();

        Sprite backgroundSprite = new Sprite("resources/sprites/background/background01.png");
        var spriteRenderer = new SpriteRenderer(backgroundSprite);
        spriteRenderer.origin = new Vector2(0, 0);
        GameObject backgroundObject = new GameObject("background");
        backgroundObject.addComponent(spriteRenderer);
        scene.addObject(backgroundObject);

        var youWinText = scene.createObject("youWinText");
        DefaultTextProvider text = new DefaultTextProvider("You Win!");
        text.setFontType(FontType.Bold);
        youWinText.addComponent(new TextRenderer(text));
        youWinText.transform.setScale(new Vector2(2f));
        youWinText.transform.setPosition(new Vector2(Painter.renderCenter.x, 25));
        youWinText.transform.setPosition(youWinText.transform.getPosition().subtract(new Vector2(Painter.measureString("You Win!").x * youWinText.transform.getScale().x, 0)));

        Sprite textBackgroundSprite = Sprite.square(230, 25, basePallet.getColorFromIndex(2));
        var textBackground = scene.createObject("textbg");
        textBackground.addComponent(new SpriteRenderer(textBackgroundSprite));
        textBackground.transform.setPosition(Painter.renderCenter);
        textBackground.transform.setPosition(textBackground.transform.getPosition().add(new Vector2(0, 50)));

        var nameInput = scene.createObject("nameInput");
        InputRenderer nameInputRenderer = new InputRenderer(25);
        nameInputRenderer.allowAllCharacters(true);
        nameInputRenderer.onlyCapitalLetters = false;
        nameInput.addComponent(nameInputRenderer);
        nameInput.transform.setPosition(textBackground.transform.getPosition()
                .add(new Vector2(0, 10).subtract(new Vector2(13, 0))));

        var enterNameText = scene.createObject("enterNameText");
        enterNameText.addComponent(new TextRenderer(new DefaultTextProvider("Enter your name:")));
        enterNameText.transform.setPosition(nameInput.transform.getPosition());
        enterNameText.transform.setPosition(enterNameText.transform.getPosition().subtract(new Vector2(103, 37)));

        var currentScoreText = scene.createObject("currentScoreText");
        currentScoreText.addComponent(new TextRenderer(new DefaultTextProvider("Your current Score: " + ScoreManager.getInstance()
                .getCurrentScore())));
        currentScoreText.transform.setPosition(nameInput.transform.getPosition());
        currentScoreText.transform.setPosition(currentScoreText.transform.getPosition().subtract(new Vector2(103, 57)));

        var saveButtonObj = scene.createObject("saveButton");
        var saveButton = new Button(Sprite.square(80, 25, Color.white));
        saveButton.text = new DefaultTextProvider("Save score");
        saveButton.text.setColor(Color.black);
        saveButtonObj.addComponent(saveButton);
        saveButtonObj.transform.setPosition(nameInput.transform.getPosition());
        saveButtonObj.transform.setPosition(saveButtonObj.transform.getPosition()
                .subtract(new Vector2(103, 0).subtract(new Vector2(saveButton.getSprite().getWidth() / 2 + 2, 30))));
        saveButton.onClick.add(button -> {
            String name = nameInputRenderer.getInputAsString();
            String result = validateName(name);
            if (result.equals("INVALID"))
            {
                DialogBoxManager.getInstance()
                        .enqueue("Invalid name", "a name of '%s' is not valid, please choose another".formatted(name), 4);
                return;
            }
            if (result.equals("TAKEN"))
            {
                DialogBoxManager.getInstance()
                        .enqueue("Already Taken", ("If this isnt you, then be kind and dont override their score.\n" +
                                "If this is you however, Click 'Confirm' to override your score!").formatted(name), box -> {
                            if(box.getResult())
                            {
                                ScoreManager.getInstance().overrideScore(level, name);
                                TimedDialogBox boxx = new TimedDialogBox("Successful!", "Your scores have been saved!", 3);
                                boxx.continuation.add(b -> {
                                    DialogBoxManager.getInstance().clearAll(true);
                                    Application.current().loadScene("LevelSelect");
                                });
                                DialogBoxManager.getInstance().enqueue(boxx);
                            }
                        });
                return;
            }

            ScoreManager.getInstance().setCurrentPlayerName(name);
            ScoreManager.getInstance().saveCurrentScore(level);
            ScoreManager.getInstance().resetScore();

            TimedDialogBox box = new TimedDialogBox("Successful!", "Your scores have been saved!", 3);
            box.continuation.add(b -> {
                DialogBoxManager.getInstance().clearAll(true);
                Application.current().loadScene("LevelSelect");
            });
            DialogBoxManager.getInstance().enqueue(box);


        });

        var disardButtonObj = scene.createObject("saveButton");
        var disardButton = new Button(Sprite.square(95, 25, Color.white));
        disardButton.text = new DefaultTextProvider("Discard score");
        disardButton.text.setColor(Color.black);
        disardButtonObj.addComponent(disardButton);
        disardButtonObj.transform.setPosition(nameInput.transform.getPosition());
        disardButtonObj.transform.setPosition(disardButtonObj.transform.getPosition()
                .add(new Vector2(31, 0).add(new Vector2(disardButton.getSprite()
                        .getWidth() / 2 + 2, 0).add(new Vector2(0, 30)))));
        disardButton.onClick.add(button -> {
            ConfirmationDialogBox box = new ConfirmationDialogBox("Attention", "Are you sure you want to discard your scores?", b -> {
                if(b.getResult())
                {
                    ScoreManager.getInstance().resetScore();
                    DialogBoxManager.getInstance().clearAll(true);
                    Application.current().loadScene("LevelSelect");

                }
            });
           DialogBoxManager.getInstance().enqueue(box);
        });

        scene.setScenePallet(pallet);
    }

    private static String validateName(String name)
    {
        final String invalid = "INVALID";
        final String taken = "TAKEN";

        if(name == null)
            return invalid;
        if(name.isBlank())
            return invalid;

        var scores = ScoreManager.getInstance().getScores(level);
        if(scores.stream().anyMatch((ScoreManager.PlayerScore score) -> score.getName().equals(name)))
            return taken;

        return "OK";
    }
}
