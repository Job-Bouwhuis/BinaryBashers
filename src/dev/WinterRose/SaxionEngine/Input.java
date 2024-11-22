package dev.WinterRose.SaxionEngine;

import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Input
{
    private static final Set<Integer> keysHeld = new HashSet<>();
    private static final Set<Integer> keysPressed = new HashSet<>();
    private static final Set<Integer> keysReleased = new HashSet<>();

    private static Point mousePosition = new Point(0, 0);
    private static Point lastMousePosition = new Point(0, 0);
    private static int scrollDelta = 0;

    private static boolean mouseLeftHold;
    private static boolean mouseLeftClick;
    private static boolean mouseLeftRelease;

    private static boolean mouseRightHold;
    private static boolean mouseRightClick;
    private static boolean mouseRightRelease;

    private static boolean mouseMiddleHold;
    private static boolean mouseMiddleClick;
    private static boolean mouseMiddleRelease;

    public static void mouseEvent(MouseEvent event)
    {
        if (event.isLeftMouseButton())
        {
            if (event.isMouseDown())
            {
                mouseLeftClick = true;
            }
            else
            {
                mouseLeftHold = mouseLeftClick = false;
                mouseLeftRelease = true;
            }
        }

        if (event.isRightMouseButton())
        {
            if (event.isMouseDown())
            {
                mouseRightClick = true;
            }
            else
            {
                mouseRightHold = mouseRightClick = false;
                mouseRightRelease = true;
            }
        }

        if (event.isMiddleMouseButton())
        {
            if (event.isMouseDown())
            {
                mouseMiddleClick = true;
            }
            else
            {
                mouseMiddleHold = mouseMiddleClick = false;
                mouseMiddleRelease = true;
            }
        }
    }

    static void keyboardEvent(KeyboardEvent event)
    {
        int keyCode = event.getKeyCode();

        if (event.isKeyPressed())
        {
            if (!keysHeld.contains(keyCode))
                keysPressed.add(keyCode);
            keysHeld.add(keyCode);
        }
        else
        {
            if (keysHeld.contains(keyCode))
                keysHeld.remove(keyCode);
            keysReleased.add(keyCode);
        }
    }

    public static Point getMousePosition()
    {
        return new Point(mousePosition);
    }

    public static boolean mousePositionChanged()
    {
        return !mousePosition.equals(lastMousePosition);
    }

    public static int getScrollDelta()
    {
        return scrollDelta;
    }

    public static boolean getKey(Keys key)
    {
        for (int keyCode : keysHeld)
        {
            if (key.matches((char) keyCode))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean getKeyDown(Keys key)
    {
        for (int keyCode : keysPressed)
        {
            if (key.matches((char) keyCode))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean getKeyUp(Keys key)
    {
        for (int keyCode : keysReleased)
        {
            if (key.matches((char) keyCode))
            {
                return true;
            }
        }
        return false;
    }

    public static void update()
    {
        keysPressed.clear();
        keysReleased.clear();

        mouseLeftRelease = false;
        mouseRightRelease = false;
        mouseMiddleRelease = false;

        if (mouseLeftClick)
        {
            mouseLeftClick = false;
            mouseLeftHold = true;
        }

        if (mouseRightClick)
        {
            mouseRightClick = false;
            mouseRightHold = true;
        }

        if (mouseMiddleClick)
        {
            mouseMiddleClick = false;
            mouseMiddleHold = true;
        }

    }

    public static boolean getMouseDown(MouseButton mouseButton)
    {
        return switch (mouseButton)
        {
            case Left -> mouseLeftClick;
            case Middle -> mouseMiddleClick;
            case Right -> mouseRightClick;
        };
    }

    public static boolean getMouse(MouseButton mouseButton)
    {
        return switch (mouseButton)
        {
            case Left -> mouseLeftHold;
            case Middle -> mouseMiddleHold;
            case Right -> mouseRightHold;
        };
    }

    public static boolean getMouseUp(MouseButton mouseButton)
    {
        return switch (mouseButton)
        {
            case Left -> mouseLeftRelease;
            case Middle -> mouseMiddleRelease;
            case Right -> mouseRightRelease;
        };
    }
}
