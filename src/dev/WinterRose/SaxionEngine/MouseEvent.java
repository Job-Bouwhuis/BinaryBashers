package dev.WinterRose.SaxionEngine;

public class MouseEvent
{
    public MouseButton button;
    public boolean clicked;
    public boolean released;
    public Vector2 location;
    public boolean isDoubleClick;

    public boolean withShift = false;
    public boolean withControl = false;
    public boolean withAlt = false;

    public boolean mouseEnteredGameWindow = false;
    public boolean mouseExitedGameWindow = false;

    public MouseEvent(MouseButton button, Vector2 location, boolean isDoubleClick)
    {
        this(location);
        this.button = button;
        this.isDoubleClick = isDoubleClick;
    }

    public MouseEvent(Vector2 location)
    {
        int windowWidth = Input.getWindowSize().x;
        int windowHeight = Input.getWindowSize().y;
        int renderWidth = Painter.renderWidth;
        int renderHeight = Painter.renderHeight;

        this.location = new Vector2(
                (location.x * renderWidth) / windowWidth,
                (location.y * renderHeight) / windowHeight).subtract(Input.getWindowPosition().divide(2));

        System.out.println(Input.windowPosition.toString());
    }
}

