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

            if (!Application.current().getActiveScene().name.equals("LevelSelect"))
            {
                DialogBoxManager.getInstance()
                        .enqueue("Attention!", "Do you want to go back to the level select?\n\nYour current progress in this level will be lost!", box -> {
                            if (box.getResult())
                            {
                                box.setPlaySounds(false);
                                DialogBoxManager.getInstance().clearAll(true);
                                Application.current().loadScene("LevelSelect");
                            }
                        });
               return;
            }

            DialogBoxManager.getInstance()
                    .enqueue("Attention!", "Are you sure you wish to exit?", box -> {
                        if (box.getResult())
                        {
                            Application.current().closeGame();
                        }
                    });

        }
    }
}
