package dev.WinterRose.SaxionEngine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class Input
{
    private static final Set<Integer> keysHeld = new HashSet<>();
    private static final Set<Integer> keysPressed = new HashSet<>();
    private static final Set<Integer> keysReleased = new HashSet<>();

    static Point windowSize = new Point();
    static Vector2 windowPosition = new Vector2();

    private static Vector2 mousePosition = new Vector2(0, 0);
    private static Vector2 lastMousePosition = new Vector2(0, 0);
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
        lastMousePosition = mousePosition;
        mousePosition = event.location;

        if (event.button == MouseButton.Left)
        {
            if (event.clicked)
            {
                mouseLeftClick = true;
            }
            else
            {
                mouseLeftHold = mouseLeftClick = false;
                mouseLeftRelease = true;
            }
        }

        if (event.button == MouseButton.Right)
        {
            if (event.clicked)
            {
                mouseRightClick = true;
            }
            else
            {
                mouseRightHold = mouseRightClick = false;
                mouseRightRelease = true;
            }
        }

        if (event.button == MouseButton.Middle)
        {
            if (event.clicked)
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

    static void keyboardEvent(KeyEvent event, boolean pressed)
    {
        int keyCode = event.getKeyCode();

        if (pressed)
        {
            if (!keysHeld.contains(keyCode)) keysPressed.add(keyCode);
            keysHeld.add(keyCode);
        }
        else
        {
            if (keysHeld.contains(keyCode)) keysHeld.remove(keyCode);
            keysReleased.add(keyCode);
        }
    }

    public static Vector2 getWindowPosition()
    {
        return windowPosition;
    }

    public static Point getWindowSize()
    {
        return windowSize;
    }

    public static Vector2 getMousePosition()
    {
        return new Vector2(mousePosition);
    }

    public static Vector2 getMouseMoveDelta()
    {
        return lastMousePosition.subtract(mousePosition);
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
            case None -> false;
        };
    }

    public static boolean getMouse(MouseButton mouseButton)
    {
        return switch (mouseButton)
        {
            case Left -> mouseLeftHold;
            case Middle -> mouseMiddleHold;
            case Right -> mouseRightHold;
            case None -> false;
        };
    }

    public static boolean getMouseUp(MouseButton mouseButton)
    {
        return switch (mouseButton)
        {
            case Left -> mouseLeftRelease;
            case Middle -> mouseMiddleRelease;
            case Right -> mouseRightRelease;
            case None -> false;
        };
    }

    static void clear()
    {
        mouseLeftClick = mouseLeftHold = mouseLeftRelease = false;
        mouseRightHold = mouseRightClick = mouseRightRelease = false;

        keysHeld.clear();
        keysPressed.clear();
        keysReleased.clear();
    }
}
