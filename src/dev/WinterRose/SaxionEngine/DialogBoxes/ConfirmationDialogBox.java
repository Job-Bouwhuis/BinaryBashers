package BinaryBashers.UI.DialogBoxes;

import dev.WinterRose.SaxionEngine.Button;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import java.util.function.Consumer;

import java.awt.*;

// i know this isnt the cleanest way to implement this. maybe ill fix it in another sprint. its very likely ill want to fix it

/*
* FIX IDEA:
*   in Application, have the dialog manager, do not have the Dialog manager be a component
*   A dialog should have the option to pause the game while active
*   the dialog manager should have a method to get a confirmation, that pauses the game and creates a confirmation dialog box that when a confirmation is given, only then returns
* */

public class ConfirmationDialogBox extends DialogBox
{
    private Button confirmButton;
    private Button cancelButton;

    private boolean accepted;

    private Consumer<ConfirmationDialogBox> callback;

    public ConfirmationDialogBox(String title, String text, Consumer<ConfirmationDialogBox> onButtonClicked)
    {
        super.title = new AnimatedTextProvider(title, 1);
        super.text = new AnimatedTextProvider(text, 1);
        callback = onButtonClicked;
    }

    public ConfirmationDialogBox(TextProvider title, TextProvider text, Consumer<ConfirmationDialogBox> onButtonClicked)
    {
        super.title = title;
        super.text = text;
        callback = onButtonClicked;
    }

    @Override
    public void init(DialogBoxManager manager)
    {
        Transform confirmTransform = new Transform();
        Transform cancelTransform = new Transform();

        Rectangle.Float bounds = manager.owner.getComponent(BoxRenderer.class).getTargetBounds();

        float margin = 3f;
        int buttonWidth = 60;
        int buttonHeight = 25;
        float spaceBetweenButtons = 3f;

        float confirmX = bounds.x + bounds.width - buttonWidth - margin;
        float confirmY = bounds.y + bounds.height - buttonHeight - margin;
        confirmTransform.setPosition(new Vector2(confirmX, confirmY));

        float cancelX = confirmX - buttonWidth - spaceBetweenButtons;
        float cancelY = confirmY;
        cancelTransform.setPosition(new Vector2(cancelX, cancelY));

        confirmButton = new Button(Sprite.square(buttonWidth, buttonHeight, Color.white));
        cancelButton = new Button(Sprite.square(buttonWidth, buttonHeight, Color.white));

        confirmButton.transform = confirmTransform;
        cancelButton.transform = cancelTransform;

        confirmButton.onClick.add(b -> {
            accepted = true;
            finish();
            callback.accept(this);
        });
        cancelButton.onClick.add(b -> {
            accepted = false;
            finish();
            callback.accept(this);
        });

        confirmButton.awake();
        cancelButton.awake();

        confirmButton.text.setText("Confirm");
        cancelButton.text.setText("Cancel");
    }

    public boolean getResult()
    {
        return accepted;
    }

    @Override
    public void update()
    {
        if(finished)
            return;
        
        confirmButton.update();
        cancelButton.update();
    }

    @Override
    public void render(Painter painter)
    {
        if(finished)
            return;

        confirmButton.render(painter);
        cancelButton.render(painter);
    }
}
