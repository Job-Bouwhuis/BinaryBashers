package BinaryBashers.Testing;

import dev.WinterRose.SaxionEngine.*;

import java.awt.*;

public class ShowMousePosOnScreen extends Renderer
{
    @Override
    public void render(Painter painter)
    {
        painter.drawText(Input.getMousePosition().toString(), new Vector2(), new Vector2(), Color.magenta);
    }
}
