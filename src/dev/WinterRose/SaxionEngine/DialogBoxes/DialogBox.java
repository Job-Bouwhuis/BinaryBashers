package dev.WinterRose.SaxionEngine.DialogBoxes;

import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.TextProviders.AnimatedTextProvider;
import dev.WinterRose.SaxionEngine.TextProviders.TextProvider;

public abstract class DialogBox
{
    protected TextProvider title;
    protected TextProvider text;

    protected final boolean haltGame;

    protected boolean finished = false;

    protected DialogBox(String title, String text, boolean haltGame)
    {
        this.title = new AnimatedTextProvider(title, 1);
        this.text = new AnimatedTextProvider(text, 1);
        this.haltGame = haltGame;
    }

    protected DialogBox(TextProvider title, TextProvider text, boolean haltGame)
    {
        this.title = title;
        this.text = text;
        this.haltGame = haltGame;
    }

    public TextProvider getTitle()
    {
        return title;
    }

    public TextProvider getText()
    {
        return text;
    }

    protected DialogBox()
    {
        haltGame = false;
    }

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
