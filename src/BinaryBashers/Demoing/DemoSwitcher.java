package BinaryBashers.Demoing;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.awt.*;

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

        if(Input.getKeyDown(Keys.P))
        {
            Scene scene = Application.getInstance().getActiveScene();
            var pallet = scene.getScenePallet();
            ColorPallet main = new ColorPallet(new Sprite("resources/colorPallets/main.png"));
            Color firstMain = main.getColorFromIndex(0);
            Color firstCur = pallet.getColorFromIndex(0);
            if(firstMain.getRGB() == firstCur.getRGB())
            {
                scene.setScenePallet(new ColorPallet(new Sprite("resources/colorPallets/midnightAblaze/midnight-ablaze.png")));
            }
            else
            {
                scene.setScenePallet(new ColorPallet(pallet.getToColors(), main.getFromColors()));
            }
        }
    }
}
