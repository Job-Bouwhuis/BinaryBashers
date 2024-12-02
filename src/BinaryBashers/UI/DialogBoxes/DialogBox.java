package BinaryBashers.UI.DialogBoxes;

import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.DefaultTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;

public abstract class DialogBox
{
    protected TextProvider title;
    protected TextProvider text;

    protected boolean finished = false;

    protected DialogBox(String title, String text)
    {
        this.title = new AnimatedTextProvider(title, 1);
        this.text = new AnimatedTextProvider(text, 1);
    }

    protected DialogBox(TextProvider title, TextProvider text)
    {
        this.title = title;
        this.text = text;
    }

    public TextProvider getTitle()
    {
        return title;
    }

    public TextProvider getText()
    {
        return text;
    }

    protected DialogBox() { }

    public abstract void init(DialogBoxManager manager);
    public abstract void update();

    /**
     * To make it possible to render stuff inside the dialog box, but not forcing it
     * @param painter
     */
    public void render(Painter painter) {}

    public boolean isFinished()
    {
        return finished;
    }

    protected void finish()
    {
        finished = true;
    }
}
