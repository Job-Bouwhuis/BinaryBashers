package dev.WinterRose.SaxionEngine.DialogBoxes;

import dev.WinterRose.SaxionEngine.Action;
import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.Time;

public class TimedDialogBox extends DialogBox
{
    private float timeout = 5;
    private float time = 0;
    public final Action<TimedDialogBox> continuation = new Action<>();

    public TimedDialogBox()
    {
        super("This is a timed dialog.", "It will go away in 5 seconds!", true);
    }

    public TimedDialogBox(String title, String text, float time)
    {
        super(title, text, true);
        this.timeout = time;

        var app = Application.current();
        if (app == null)
        {
            getText().setColor(new ColorPallet().getColorFromIndex(5));
            return;
        }

        var scene = app.getActiveScene();
        if (scene == null)
        {
            getText().setColor(new ColorPallet().getColorFromIndex(5));
            return;
        }

        var pallet = scene.getScenePallet();
        getText().setColor(pallet.getColorFromIndex(5));
    }

    public TimedDialogBox(TextProvider title, TextProvider text, float time)
    {
        super(title, text, true);
        this.timeout = time;
    }

    @Override
    public void init(DialogBoxManager manager)
    {
    }

    @Override
    public void update()
    {
        time += Time.getDeltaTime();
        if(time >= timeout)
            finish();
    }

    @Override
    protected void finish()
    {
        if(continuation.any())
            continuation.invoke(this);
        super.finish();
    }
}
