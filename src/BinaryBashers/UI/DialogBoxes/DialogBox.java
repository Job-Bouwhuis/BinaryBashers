package BinaryBashers.UI.DialogBoxes;

import BinaryBashers.UI.TextProviders.DefaultTextProvider;
import BinaryBashers.UI.TextProviders.TextProvider;

public abstract class DialogBox
{
    protected TextProvider title;
    protected TextProvider text;

    protected boolean finished = false;

    protected DialogBox(String title, String text)
    {
        this.title = new DefaultTextProvider(title);
        this.text = new DefaultTextProvider(text);
    }

    protected DialogBox(TextProvider title, TextProvider text)
    {
        this.title = title;
        this.text = text;
    }

    protected DialogBox() { }

    public abstract void update();

    public boolean isFinished()
    {
        return finished;
    }

    protected void finish()
    {
        finished = true;
    }
}
