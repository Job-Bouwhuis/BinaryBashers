package dev.WinterRose.SaxionEngine;

import BinaryBashers.UI.DialogBoxes.ConfirmationDialogBox;
import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import nl.saxion.app.SaxionApp;
import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Application implements GameLoop
{
    private static Application instance;
    public static Application getInstance()
    {
        return instance;
    }

    Map<String, Consumer<Scene>> scenes = new HashMap<>();
    Scene activeScene;
    private boolean isFullscreen = true;
    private long lastFrameTime = System.nanoTime();
    private JFrame gameWindow;

    private Point initialWindowSize;

    public Application(boolean fullscreen)
    {
        isFullscreen = fullscreen;
        instance = this;
    }

    public void run(int width, int height)
    {
        initialWindowSize = new Point(width, height);
        SaxionApp.startGameLoop(this, width, height, 1);
    }

    /**
     * Use method createScene(String, Consumer) to add scenes. then loadScene(String) to load the first scene.
     */
    public abstract void createScenes();

    public abstract void createPrefabs();

    @Override
    public void init()
    {
        updateSaxionAppReferences(initialWindowSize);
        createScenes();
    }

    public void createScene(String name, Consumer<Scene> sceneConfigurer)
    {
        scenes.put(name, sceneConfigurer);
    }

    @Override
    public void loop()
    {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
        lastFrameTime = currentTime;

        forceQuitDialog();

        Time.update(deltaTime);

        activeScene.updateScene();
        activeScene.drawScene();

        if(Input.getKey(Keys.F11))
        {
            isFullscreen = !isFullscreen;
            Input.clear();
            updateSaxionAppReferences(initialWindowSize);
            activeScene.createNewPainter();
        }

        Input.update();
        var bounds = gameWindow.getBounds();
        Input.windowPosition = new Vector2(bounds.x, bounds.y);
        Input.windowSize = new Point(bounds.width, bounds.height);
    }

    /**
     * This method is a hacky solution to the way the DialogBoxMan011ager is currently implemented.
     * In a next sprint this will be fixed
     */
    private void forceQuitDialog()
    {
        if(DialogBoxManager.getInstance() == null)
        {
            GameObject dialogManager = new GameObject("DialogBoxManager");
            dialogManager.transform.setPosition(Painter.renderCenter);
            dialogManager.addComponent(new DialogBoxManager());
            activeScene.addObject(dialogManager);
        }

        if(Input.getKey(Keys.DELETE) && Input.getKeyDown(Keys.TAB))
        {
            ConfirmationDialogBox box = new ConfirmationDialogBox("Warning!", "Are you sure you want to force quit the game?\n" +
                    "Any unsaved progress will be lost!", cdb -> {
                if(cdb.getResult())
                    Application.getInstance().closeGame();
            });
            box.getTitle().setColor(Color.red);
            DialogBoxManager.getInstance().enqueue(box);
        }
    }

    public void closeGame()
    {
         System.exit(0);
    }

    public void loadScene(String sceneName)
    {
        Consumer<Scene> sceneConfigurer = scenes.get(sceneName);
        if (sceneConfigurer == null) throw new RuntimeException("No scene with name: " + sceneName);

        Scene scene = new Scene(sceneName);

        try
        {
            sceneConfigurer.accept(scene);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to create scene", e);
        }

        activeScene = scene;
        activeScene.wakeScene();
    }

    @Override
    public void keyboardEvent(KeyboardEvent e)
    {
        System.out.println(e.getKeyCode());
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
        if (activeScene != null) activeScene.handleCallbacks(event);
    }

    public boolean isFullscreen()
    {
        return isFullscreen;
    }

    private void updateSaxionAppReferences(Point windowSize)
    {
        Panel canvas = createCustomFrame(windowSize);

        for (var mouseListener : canvas.getMouseListeners())
            canvas.removeMouseListener(mouseListener);
        for (var mouseMotion : canvas.getMouseMotionListeners())
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

        canvas.addMouseMotionListener(new MouseMotionListener()
        {
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

    private Panel createCustomFrame(Point windowSize)
    {
        try
        {
            Class<SaxionApp> app = SaxionApp.class;

            Field frameField = app.getDeclaredField("frame");
            frameField.setAccessible(true);
            ((JFrame) frameField.get(null)).setVisible(false);

            JFrame frame = new JFrame();
            Field keyListener = app.getDeclaredField("keyListener");
            keyListener.setAccessible(true);

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
            int screenWidth = screenBounds.width;
            int screenHeight = screenBounds.height;

            int width = 0;
            int height = 0;
            if (isFullscreen)
            {
                frame.setUndecorated(true);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                width = screenWidth;
                height = screenHeight;
            }
            else
            {
                frame.setUndecorated(false);
                width = windowSize.x;
                height = windowSize.y;
            }


            frame.setLayout(null);
            frame.addKeyListener((KeyListener) keyListener.get(null));
            frame.setFocusable(true);
            frame.setFocusTraversalKeysEnabled(false);
            frame.setDefaultCloseOperation(3);
            frame.getContentPane().setPreferredSize(new Dimension(width, height));
            frame.pack();
            frame.setResizable(false);

            if (!isFullscreen)
                frame.setLocation((screenWidth / 2) - (frame.getWidth() / 2), (screenHeight / 2) - (frame.getHeight() / 2));

            Field canvasField = app.getDeclaredField("canvas");
            canvasField.setAccessible(true);
            Panel canvas = (Panel) canvasField.get(null);
            frame.setContentPane(canvas);

            frameField.set(null, frame);

            Thread.sleep(100);
            frame.setVisible(true);

            gameWindow = frame;

            return canvas;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}






