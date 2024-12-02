package BinaryBashers.UI.DialogBoxes;

import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.Time;

public class TimedDialogBox extends DialogBox
{
    private float timeout = 5;
    private float time = 0;

    public TimedDialogBox()
    {
        super("This is a timed dialog.", "It will go away in 5 seconds!");
    }

    @Override
    public void init(DialogBoxManager manager)
    {

    }

    public TimedDialogBox(String title, String text, float time)
    {
        super(title, text);
        this.timeout = time;
    }

    public TimedDialogBox(TextProvider title, TextProvider text, float time)
    {
        super(title, text);
        this.timeout = time;
    }

    @Override
    public void update()
    {
        time += Time.deltaTime;
        if(time >= timeout)
            finish();
    }
}
