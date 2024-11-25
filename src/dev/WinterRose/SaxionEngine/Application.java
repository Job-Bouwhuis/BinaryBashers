package dev.WinterRose.SaxionEngine;

import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import org.w3c.dom.DOMError;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Application implements GameLoop
{
    ArrayList<Scene> allScenes = new ArrayList<>();
    Scene activeScene;
    private long lastFrameTime = System.nanoTime(); // last time is also time of app creation.

    public abstract Scene createScene();

    public abstract Scene[] preLoadOtherScenes();

    @Override
    public void init()
    {
        subscribeToMouseMoveEventBecauseSaxionAppDevsWereTooLazyToMakeAGoodOne();
        activeScene = createScene();
        allScenes.add(activeScene);
        allScenes.addAll(Arrays.asList(preLoadOtherScenes()));
        activeScene.wakeScene();
    }

    @Override
    public void loop()
    {
        long currentTime = System.nanoTime(); // time in nanoseconds
        float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
        lastFrameTime = currentTime;

        Time.update(deltaTime);

        activeScene.updateScene();
        activeScene.drawScene();

        Input.update();
    }

    public void changeScene(String sceneName) throws Exception
    {
        Scene target = findScene(sceneName);

        if (target == null) throw new Exception("No scene with name: " + sceneName);

        activeScene = target;
        activeScene.wakeScene();
    }

    public Scene findScene(String sceneName)
    {
        for (Scene scene : allScenes)
            if (scene.name.equals(sceneName)) return scene;
        return null;
    }

    @Override
    public void keyboardEvent(KeyboardEvent e)
    {
        Input.keyboardEvent(e);
        activeScene.handleCallbacks(e);
    }

    @Override
    public void mouseEvent(nl.saxion.app.interaction.MouseEvent e)
    {
        // really SaxionApp devs... you can do better with the MouseEvent you guys have...
        // i just made my own cause yours is so incredibly limiting :/
    }

    private void mouseEvent(MouseEvent event)
    {
        Input.mouseEvent(event);
        activeScene.handleCallbacks(event);
    }

    private void subscribeToMouseMoveEventBecauseSaxionAppDevsWereTooLazyToMakeAGoodOne()
    {
        try
        {
            Class<SaxionApp> app = SaxionApp.class;
            Field canvasField = app.getDeclaredField("canvas");
            canvasField.setAccessible(true);

            Panel canvas = (Panel) canvasField.get(null);

            for(var mouseListener : canvas.getMouseListeners())
                canvas.removeMouseListener(mouseListener);
            for(var mouseMotion : canvas.getMouseMotionListeners())
                canvas.removeMouseMotionListener(mouseMotion);

            canvas.addMouseListener(new MouseListener()
            {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e)
                {

                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent e)
                {
                    MouseButton button = switch (e.getButton())
                    {
                        case 0 -> MouseButton.None;
                        case 1 -> MouseButton.Left;
                        case 2 -> MouseButton.Middle;
                        case 3 -> MouseButton.Right;
                        default -> MouseButton.None;
                    };
                    var event = new MouseEvent(button, new Vector2(e.getXOnScreen(), e.getYOnScreen()), e.getClickCount() == 2);
                    event.clicked = true;
                    mouseEvent(event);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e)
                {
                    MouseButton button = switch (e.getButton())
                    {
                        case 0 -> MouseButton.None;
                        case 1 -> MouseButton.Left;
                        case 2 -> MouseButton.Middle;
                        case 3 -> MouseButton.Right;
                        default -> MouseButton.None;
                    };
                    var event = new MouseEvent(button, new Vector2(e.getXOnScreen(), e.getYOnScreen()), e.getClickCount() == 2);
                    event.released = false;
                    mouseEvent(event);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e)
                {
                    var event = new MouseEvent(new Vector2(e.getXOnScreen(), e.getYOnScreen()));
                    event.mouseEnteredGameWindow = true;
                    mouseEvent(event);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e)
                {
                    var event = new MouseEvent(new Vector2(e.getXOnScreen(), e.getYOnScreen()));
                    event.mouseExitedGameWindow = true;
                    mouseEvent(event);
                }

            });

            canvas.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(java.awt.event.MouseEvent e)
                {
                    doEvent(e);
                }

                @Override
                public void mouseMoved(java.awt.event.MouseEvent e)
                {
                    doEvent(e);
                }

                private void doEvent(java.awt.event.MouseEvent e)
                {
                    MouseButton button = switch (e.getButton())
                    {
                        case 0 -> MouseButton.None;
                        case 1 -> MouseButton.Left;
                        case 2 -> MouseButton.Middle;
                        case 3 -> MouseButton.Right;
                        default -> MouseButton.None;
                    };
                    var event = new MouseEvent(new Vector2(e.getXOnScreen(), e.getYOnScreen()));
                    event.button = button;
                    mouseEvent(event);
                }
            });
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }

    }
}






