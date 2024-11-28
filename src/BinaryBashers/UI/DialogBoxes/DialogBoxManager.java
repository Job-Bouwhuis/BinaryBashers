package BinaryBashers.UI.DialogBoxes;

import BinaryBashers.UI.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class DialogBoxManager extends ActiveRenderer
{
    private static DialogBoxManager instance;
    public static DialogBoxManager getInstance()
    {
        return instance;
    }

    private BoxRenderer boxRenderer;

    private TextProvider currentText;
    private DialogBox currentDialog;

    private Queue<DialogBox> dialogQueue = new Queue<>();

    @Override
    public void awake()
    {
        if(instance != null)
        {
            owner.destroy(); // a dialog box manager already exists, destroy this one so there is no conflict
        }

        if(!owner.hasComponent(BoxRenderer.class))
            owner.addComponent(new BoxRenderer(new Vector2(Painter.renderWidth / 2, Painter.renderHeight / 2)));
        boxRenderer = owner.getComponent(BoxRenderer.class);
    }

    @Override
    public void update()
    {
        if(Input.getKeyDown(Keys.SPACE))
        {
            enqueue("Cool Shit", "some testing dialog box to make sure things are working like they should", 5);
        }

        if(currentDialog != null)
        {
            if(currentDialog.isFinished())
            {
                currentDialog = null;
                if(dialogQueue.pop() instanceof DialogBox box)
                    currentDialog = box;
            }
            else
                currentDialog.update();
        }

        if(currentDialog == null)
        {
            if(dialogQueue.pop() instanceof DialogBox box)
                currentDialog = box;
            if(boxRenderer.isAnimatingIn())
                boxRenderer.animateOut();
            return;
        }
        if(!boxRenderer.isAnimatingIn())
            boxRenderer.animateIn();

        Vector2 boxWidthHeight = boxRenderer.getCurrentWidthHeight(); // (near) 0 when invisible
        float animationProgress = boxRenderer.getAnimationProgress(); // 0 near 0 when fully animated out and invisible, near 1 when fully animated in and visible
    }

    @Override
    public void render(Painter painter)
    {
        if(currentDialog == null)
            return;

        final Vector2 origin = new Vector2(0, .5f); // make it so the text placement is in the middle left of the text element.
        // some logic that smoothly and letter by letter types out the text and title
        TextProvider text = currentDialog.text;
        painter.drawText(text, transform, origin);
    }

    public void enqueue(DialogBox box)
    {
        dialogQueue.enqueue(box);
    }

    public void enqueue(String title, String text, float time)
    {
        enqueue(new TimedDialogBox(title, text, time));
    }


}
