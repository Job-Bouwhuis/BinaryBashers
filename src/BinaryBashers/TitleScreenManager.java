package BinaryBashers;

import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Behavior;
import dev.WinterRose.SaxionEngine.Input;
import dev.WinterRose.SaxionEngine.Keys;

public class TitleScreenManager extends Behavior
{

    @Override
    public void update()
    {
        if(Input.getKey(Keys.NUM_1))
        {
            Application.current().loadScene("LevelSelect");
        }

        if(Input.getKey(Keys.NUM_0))
        {
            Application.current().closeGame();
        }
    }
}
