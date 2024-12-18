package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.DialogBoxes.ConfirmationDialogBox;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Application
{
    private static Application instance;
    private boolean gameRunning;
    public Action<Application> onAppClosing = new Action();

    public static Application current()
    {
        return instance;
    }

    Map<String, Consumer<Scene>> scenes = new HashMap<>();
    Scene activeScene;
    private boolean isFullscreen = true;
    private long lastFrameTime = System.nanoTime();
    private JFrame applicationWindow;
    private Panel contentPanel;
    private Painter appPainter;
    private Point initialWindowSize;
    private Integer fps = 0;

    private Sprite screenCover;
    public Application(boolean fullscreen)
    {
        isFullscreen = fullscreen;
        instance = this;
        screenCover = Sprite.square(Painter.renderWidth, Painter.renderHeight, Color.black);
    }

    public void run(int width, int height, int targetFramerate)
    {
        gameRunning = true;
        initialWindowSize = new Point(width, height);

        prepareApplicationWindow(initialWindowSize);
        appPainter = new Painter(applicationWindow);
        createScenes();
        createPrefabs();

        long frameDuration = 1_000_000_000L / targetFramerate;
        long previousTime = System.nanoTime();
        long accumulatedTime = 0;

        int frames = 0;
        long secondTimer = System.nanoTime();

        while (gameRunning)
        {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - previousTime;
            previousTime = currentTime;

            accumulatedTime += elapsedTime;

            if (accumulatedTime >= frameDuration)
            {
                loop();
                frames++;
                accumulatedTime -= frameDuration;
            }

            if (System.nanoTime() - secondTimer >= 1_000_000_000L) {
                fps = frames;
                frames = 0;
                secondTimer += 1_000_000_000L;
            }

            try
            {
                long sleepTime = (frameDuration - (System.nanoTime() - previousTime)) / 1_000_000;
                if (sleepTime > 0)
                    Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }

        applicationWindow.setVisible(false);

        onAppClosing.invoke(this);

        applicationWindow.dispose();
    }

    public Integer getFPS()
    {
        return fps;
    }

    public void createScene(String name, Consumer<Scene> sceneConfigurer)
    {
        scenes.put(name, sceneConfigurer);
    }
    /**
     * Use method createScene(String, Consumer) to add scenes. then loadScene(String) to load the first scene.
     */
    public abstract void createScenes();

    /**
     * Used to create prefabs of game objects that can be spawned in. eg item drops, enemies, tiles, etc.
     */
    public abstract void createPrefabs();

    private void loop()
    {
        forceQuitDialog();

        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
        lastFrameTime = currentTime;

        Time.update(deltaTime);
        var dialogManager = DialogBoxManager.getInstance();

        boolean hasActiveDialogBox = dialogManager.update();

        if(!hasActiveDialogBox)
        {
            activeScene.updateScene();
        }

        appPainter.begin();

        activeScene.drawScene(appPainter);
        dialogManager.render(appPainter);

        // DEBUG STUFF
        appPainter.drawText(Input.getMousePosition().toString(), new Vector2(), new Vector2(), Color.white);
        appPainter.drawText(fps.toString(), new Vector2(), new Vector2(), Color.white);

        if (Input.getMouseUp(MouseButton.Left))
            appPainter.drawText("true", new Vector2(0, 25), new Vector2(), Color.white);
        else
            appPainter.drawText("false", new Vector2(0, 25), new Vector2(), Color.white);
        // END DEBUG
        appPainter.end();

        if (Input.getKey(Keys.F11))
        {
            isFullscreen = !isFullscreen;
            Input.clear();
            prepareApplicationWindow(initialWindowSize);
            appPainter = new Painter(applicationWindow);
        }

        Input.update();
        var bounds = applicationWindow.getBounds();
        Input.windowPosition = new Vector2(bounds.x, bounds.y);
        Input.windowSize = new Point(bounds.width, bounds.height);
    }

    /**
     * This method is a hacky solution to the way the DialogBoxMan011ager is currently implemented.
     * In a next sprint this will be fixed
     */
    private void forceQuitDialog()
    {
        if (Input.getKey(Keys.ALT) && Input.getKeyDown(Keys.F4))
        {
            ConfirmationDialogBox box = new ConfirmationDialogBox("Warning!", "Are you sure you want to force quit the game?\n" + "Any unsaved progress will be lost!", cdb -> {
                if (cdb.getResult()) Application.current().closeGame();
            });
            box.getTitle().setColor(Color.red);
            DialogBoxManager.getInstance().enqueue(box);
        }
    }

    public void closeGame()
    {
        gameRunning = false;
    }

    public void loadScene(String sceneName)
    {
        Transform screenCoverPosition = new Transform();
        screenCoverPosition.setPosition(new Vector2(0, -Painter.renderHeight));
        while(true)
        {
            appPainter.begin();
            if(activeScene != null)
                activeScene.drawScene(appPainter);
            screenCoverPosition.translateY(200 * Time.getUnscaledDeltaTime());
            appPainter.end();

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
            lastFrameTime = currentTime;

            Time.update(deltaTime);

            if(screenCoverPosition.getPosition().y > 0)
            {
                screenCoverPosition.setPosition(new Vector2());
                break;
            }
        }

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

        while(true)
        {
            appPainter.begin();
            if(activeScene != null)
                activeScene.drawScene(appPainter);
            screenCoverPosition.translateY(200 * Time.getUnscaledDeltaTime());
            appPainter.drawSprite(screenCover, screenCoverPosition, new Vector2(), Color.white);
            appPainter.end();

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
            lastFrameTime = currentTime;

            Time.update(deltaTime);

            if(screenCoverPosition.getPosition().y > Painter.renderHeight)
            {
                screenCoverPosition.setPosition(new Vector2(0, Painter.renderHeight));
                break;
            }
        }
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

    private void prepareApplicationWindow(Point windowSize)
    {
        JFrame canvas = createCustomFrame(windowSize);

        // Reset any previous listeners to avoid duplication
        for (var mouseListener : canvas.getMouseListeners())
            canvas.removeMouseListener(mouseListener);
        for (var mouseMotion : canvas.getMouseMotionListeners())
            canvas.removeMouseMotionListener(mouseMotion);

        // Set a proper layout manager
        canvas.setLayout(new BorderLayout());

        // Add mouse listener
        canvas.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {}

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
                event.released = true;
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

        // Add mouse motion listener
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


    private JFrame createCustomFrame(Point windowSize)
    {
        try
        {
            applicationWindow = new JFrame();

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
            int screenWidth = screenBounds.width;
            int screenHeight = screenBounds.height;

            int width;
            int height;
            if (isFullscreen)
            {
                applicationWindow.setUndecorated(true);
                applicationWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
                width = screenWidth;
                height = screenHeight;
            }
            else
            {
                applicationWindow.setUndecorated(false);
                width = windowSize.x;
                height = windowSize.y;
            }

            applicationWindow.setLayout(null);
            applicationWindow.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                    if(activeScene != null)
                        activeScene.handleCallbacks(e);
                }

                public void keyPressed(KeyEvent e) {
                    Input.keyboardEvent(e, true);
                }

                public void keyReleased(KeyEvent e) {
                    Input.keyboardEvent(e, false);
                }
            });

            applicationWindow.setFocusable(true);
            applicationWindow.setFocusTraversalKeysEnabled(false);
            applicationWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            applicationWindow.getContentPane().setPreferredSize(new Dimension(width, height));
            applicationWindow.pack();
            applicationWindow.setResizable(false);

            if (!isFullscreen)
                applicationWindow.setLocation((screenWidth / 2) - (applicationWindow.getWidth() / 2), (screenHeight / 2) - (applicationWindow.getHeight() / 2));

            applicationWindow.setVisible(true);

            applicationWindow.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    if(activeScene != null)
                    {
                        if(activeScene.name.equals("LevelSelect"))
                        {
                            closeGame();
                            return;
                        }
                    }

                    ConfirmationDialogBox box = new ConfirmationDialogBox("Warning!", "Are you sure you want to force quit the game?\n" + "Any unsaved progress will be lost!", cdb -> {
                        if (cdb.getResult()) Application.current().closeGame();
                    });
                    box.getTitle().setColor(Color.red);
                    DialogBoxManager.getInstance().enqueue(box);
                }
            });

            return applicationWindow;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public Scene getActiveScene()
    {
        return activeScene;
    }

    public JFrame getWindow()
    {
        return applicationWindow;
    }
}






