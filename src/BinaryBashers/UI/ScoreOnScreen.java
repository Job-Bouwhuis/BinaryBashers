package BinaryBashers.UI;

import BinaryBashers.Enemies.ScoreManager;
import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Behavior;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.TextRenderer;
import dev.WinterRose.SaxionEngine.Vector2;

public class ScoreOnScreen extends Behavior
{
    private TextRenderer text;
    private ScoreManager scores;

    @Override
    public void awake()
    {
        scores = ScoreManager.getInstance();
        if(owner.hasComponent(TextRenderer.class))
            text = owner.getComponent(TextRenderer.class);
        else
            text = (TextRenderer)owner.addComponent(new TextRenderer(""));

        text.setTextColor(Application.current().getActiveScene().getScenePallet().getColorFromIndex(6));

        transform.setPosition(new Vector2());
    }

    @Override
    public void update()
    {
        text.setText("Score: " + scores.getCurrentScore());
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        if(text == null)
            return;
        text.setTextColor(colorPallet.getColorFromIndex(6));
    }
}
