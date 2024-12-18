package BinaryBashers;

import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Behavior;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.Input;
import dev.WinterRose.SaxionEngine.Keys;

public class BackToMainMenuComponent extends Behavior
{
    @Override
    public void update()
    {
        if (Input.getKeyDown(Keys.ESCAPE))
        {
            DialogBoxManager.getInstance()
                    .enqueue("Attention!", "Do you want to go back to the level select?\n\nYour current progress in this level will be lost!", box -> {
                        if (box.getResult())
                        {
                            Application.getInstance().loadScene("LevelSelect");
                        }
                    });
        }
    }
}
