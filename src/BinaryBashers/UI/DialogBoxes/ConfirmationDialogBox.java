package BinaryBashers.UI.DialogBoxes;

import BinaryBashers.UI.TextProviders.TextProvider;
import dev.WinterRose.SaxionEngine.Input;
import dev.WinterRose.SaxionEngine.Keys;

public class ConfirmationDialogBox extends DialogBox
{
    public ConfirmationDialogBox()
    {
        super("Confirm", "Confirm to continue!");
    }

    public ConfirmationDialogBox(String title, String text)
    {
        super(title, text);
    }

    public ConfirmationDialogBox(TextProvider title, TextProvider text)
    {
        super(title, text);
    }

    @Override
    public void update()
    {
        if (Input.getKeyDown(Keys.SPACE)) finish();
    }
}
