package BinaryBashers.UI.DialogBoxes;

import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.*;

import java.awt.*;
import java.util.function.Consumer;

public class DialogBoxManager extends ActiveRenderer
{
    private static DialogBoxManager instance;
    public static DialogBoxManager getInstance()
    {
        return instance;
    }

    private Sound dialogEnterSound;
    private Sound dialogExitSound;

    private BoxRenderer boxRenderer;

    private DialogBox currentDialog;

    private Queue<DialogBox> dialogQueue = new Queue<>();

    @Override
    public void awake()
    {
        if(instance != null)
            owner.destroy(); // a dialog box manager already exists, destroy this one so there is no conflict

        if(!owner.hasComponent(BoxRenderer.class))
            owner.addComponent(new BoxRenderer(new Vector2(Painter.renderWidth / 2, Painter.renderHeight / 2)));
        boxRenderer = owner.getComponent(BoxRenderer.class);

        dialogEnterSound = new Sound("resources/audio/8bit-DialogEnter.wav");
        dialogEnterSound.setVolume(.6f);
        dialogExitSound = new Sound("resources/audio/8bit-DialogExit.wav");
        dialogExitSound.setVolume(.6f);
        instance = this;
    }

    @Override
    public void update()
    {
        if(currentDialog != null)
        {
            if(currentDialog.isFinished())
            {
                currentDialog = null;
                if(dialogQueue.pop() instanceof DialogBox box)
                    currentDialog = box;
            }
            else if(boxRenderer.getAnimationProgress() > .96f)
                currentDialog.update();
        }

        if(currentDialog == null)
        {
            if(dialogQueue.pop() instanceof DialogBox box)
            {
                currentDialog = box;
                box.init(this);
            }
            if(boxRenderer.isAnimatingIn())
            {
                boxRenderer.animateOut();
                dialogExitSound.play();
            }
            return;
        }
        if(!boxRenderer.isAnimatingIn())
        {
            boxRenderer.animateIn();
            dialogEnterSound.play();
        }
    }

    @Override
    public void render(Painter painter)
    {
        if(currentDialog == null)
            return;

        final Vector2 origin = new Vector2(0, 0);

        var bounds = boxRenderer.getCurrentBounds();
        Vector2 boxTopLeft = new Vector2(
                transform.getPosition().x - bounds.width / 2,
                transform.getPosition().y - bounds.height / 2);

        if(boxRenderer.isAnimatingIn() && boxRenderer.getAnimationProgress() > .96f)
        {
            currentDialog.render(painter);

            // title
            {
                Transform t = new Transform();
                t.setPosition(new Vector2(bounds.x, bounds.y).subtract(new Vector2(boxTopLeft.x, boxTopLeft.y).subtract(new Vector2(3, 0))));
                if(currentDialog.title.getColor() == Color.white)
                    currentDialog.title.setColor(Color.cyan);
                painter.drawText(currentDialog.title, t, origin, bounds);
            }

            if(currentDialog.title instanceof AnimatedTextProvider atp && atp.getAnimationPercent() < .7f)
                return;

            // text
            {
                Transform t = new Transform();
                t.setPosition(new Vector2(bounds.x, bounds.y).subtract(new Vector2(boxTopLeft.x, boxTopLeft.y).subtract(new Vector2(3, 22))));
                painter.drawText(currentDialog.text, t, origin, bounds);
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

    @Override
    public void onDestroyed()
    {
        instance = null;
    }

    public DialogBox getCurrentDialog()
    {
        return currentDialog;
    }
}
