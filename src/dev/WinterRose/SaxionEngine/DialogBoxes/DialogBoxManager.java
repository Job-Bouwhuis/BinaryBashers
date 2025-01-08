package dev.WinterRose.SaxionEngine.DialogBoxes;

import dev.WinterRose.SaxionEngine.Queue;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

public class DialogBoxManager
{
    private static DialogBoxManager instance;

    static
    {
        instance = new DialogBoxManager();
    }

    private Sound dialogEnterSound;
    private Sound dialogExitSound;

    private BoxRenderer boxRenderer;
    private DialogBox currentDialog;

    private Queue<DialogBox> dialogQueue = new Queue<>();

    private boolean lastSoundSetting = true;

    DialogBoxManager()
    {
        if (instance != null) return;

        boxRenderer = new BoxRenderer(new Vector2(Painter.renderWidth / 2, Painter.renderHeight / 2));
        boxRenderer.transform = new Transform();
        boxRenderer.transform.setPosition(new Vector2(Painter.renderWidth / 2, Painter.renderHeight / 2));

        dialogEnterSound = new Sound("resources/audio/8bit-DialogEnter.wav");
        dialogEnterSound.setVolume(.6f);
        dialogExitSound = new Sound("resources/audio/8bit-DialogExit.wav");
        dialogExitSound.setVolume(.6f);
        instance = this;
    }

    public static DialogBoxManager getInstance()
    {
        return instance;
    }

    public boolean update()
    {
        boxRenderer.update();
        if (currentDialog != null)
        {
            lastSoundSetting = currentDialog.getPlaySounds();
            if (currentDialog.isFinished())
            {
                currentDialog = null;
                if (dialogQueue.pop() instanceof DialogBox box)
                {
                    currentDialog = box;
                    box.init(this);
                }
            }
            else if (boxRenderer.getAnimationProgress() > .96f) currentDialog.update();
        }

        if (currentDialog == null)
        {
            if (dialogQueue.pop() instanceof DialogBox box)
            {
                currentDialog = box;
                box.init(this);
            }
            if (boxRenderer.isAnimatingIn())
            {
                boxRenderer.animateOut();
                if(lastSoundSetting)
                    dialogExitSound.play();
            }
            return false;
        }
        if (!boxRenderer.isAnimatingIn())
        {
            boxRenderer.animateIn();
            if(currentDialog.getPlaySounds())
                dialogEnterSound.play();
        }
        return true;
    }

    public void render(Painter painter)
    {
        boxRenderer.render(painter);

        if (currentDialog == null) return;

        if (boxRenderer.isAnimatingIn() && boxRenderer.getAnimationProgress() > .96f)
        {
            currentDialog.render(painter);
            var bounds = boxRenderer.getTargetBounds();

            // title
            {
                Transform t = new Transform();
                t.setPosition(t.getPosition().add(new Vector2(3, 0)));
                if (currentDialog.title.getColor() == Color.white) currentDialog.title.setColor(Color.cyan);
                painter.drawText(currentDialog.title, t, new Vector2(0, 0), bounds);
            }

            if (currentDialog.title instanceof AnimatedTextProvider atp && atp.getAnimationPercent() < .7f) return;

            // text
            {
                Transform t = new Transform();
                t.setPosition(t.getPosition().add(new Vector2(3, 22)));
                painter.drawText(currentDialog.text, t, new Vector2(0, 0), bounds);
            }
        }
    }

    public void enqueue(DialogBox box)
    {
        dialogQueue.enqueue(box);
    }

    public void enqueue(String title, String text, float time)
    {
        enqueue(new TimedDialogBox(title, text, time));
    }

    public void enqueue(String title, String text, Consumer<ConfirmationDialogBox> onButtonClicked)
    {
        enqueue(new ConfirmationDialogBox(title, text, onButtonClicked));
    }

    public DialogBox getCurrentDialog()
    {
        return currentDialog;
    }

    public Rectangle2D.Float getBoxCurrentBounds()
    {
        return boxRenderer.getCurrentBounds();
    }

    public Rectangle2D.Float getBoxTargetBounds()
    {
        return boxRenderer.getTargetBounds();
    }

    public void clearAll(boolean forceHideBoxRenderer)
    {
        dialogQueue.clear();

        if (forceHideBoxRenderer) boxRenderer.hideImmediately();
    }

    public void enqueue(String title, String text, Consumer<ConfirmationDialogBox> onButtonClicked, boolean playSounds)
    {
        var box = new ConfirmationDialogBox(title, text, onButtonClicked);
        box.setPlaySounds(playSounds);
        enqueue(box);
    }
}
