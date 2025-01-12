package BinaryBashers.UI;

import dev.WinterRose.SaxionEngine.*;

import java.awt.*;

public class HealthImage
{
    public Sprite heartImage = new Sprite("resources/sprites/ui/Heart.png");
    private final Vector2 position;
    private final Transform transform;
    private final Vector2 origin = new Vector2(.5f, .5f);

    private Color tint = Color.white;

    public void setAlive(boolean alive)
    {
        if(alive)
            tint = Color.white;
        else {
            heartImage = heartImage.getSolid();
            tint = Application.current().getActiveScene().getScenePallet().getColorFromIndex(1);
        }
    }

    public HealthImage(Vector2 position, Vector2 scale)
    {
        this.position = position;

        transform = new Transform();
        transform.setPosition(position);
        transform.setScale(scale);
    }

    public void paint(Painter painter)
    {
        painter.drawSprite(heartImage, transform, origin, tint);
    }

    public Vector2 getPosition()
    {
        return position;
    }
}
