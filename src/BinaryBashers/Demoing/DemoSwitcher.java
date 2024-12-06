package BinaryBashers.Demoing;

import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Behavior;
import dev.WinterRose.SaxionEngine.Input;
import dev.WinterRose.SaxionEngine.Keys;

public class DemoSwitcher extends Behavior
{
    @Override
    public void update()
    {
        if(Input.getKeyDown(Keys.F5))
        {
            Application.getInstance().loadScene("spawnerTest");
        }

        if(Input.getKeyDown(Keys.F6))
        {
            Application.getInstance().loadScene("childParentDemo");
        }
    }
}
