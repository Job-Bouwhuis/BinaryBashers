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
        final Vector2 JFrameOffsetCompensation = new Vector2(8, 30);
        location = location.subtract(JFrameOffsetCompensation); // workaround for slightly off location values from the JFrame side
        int windowWidth = Input.getWindowSize().x;
        int windowHeight = Input.getWindowSize().y;
        int renderWidth = Painter.renderWidth;
        int renderHeight = Painter.renderHeight;

        this.location = new Vector2(
                (location.x * renderWidth) / windowWidth,
                (location.y * renderHeight) / windowHeight).subtract(Input.getWindowPosition().divide(2));

        if(Application.getInstance().isFullscreen())
            this.location = this.location.add(new Vector2(2, 7.5f)); // account for the window decoration not being there now.
    }
}

