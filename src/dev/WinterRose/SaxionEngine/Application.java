package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.DialogBoxes.ConfirmationDialogBox;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

public abstract class Application
{
    private static Application instance;
    public Action<Application> onAppClosing = new Action();
    public boolean showFPS = false;
    Map<String, Consumer<Scene>> scenes = new HashMap<>();
    Scene activeScene;
    boolean finishedFrame = true;
    private boolean gameRunning;
    private boolean isFullscreen = true;
    private long lastFrameTime = System.nanoTime();
    private JFrame applicationWindow;
    private Panel contentPanel;
    private Painter appPainter;
    private Point initialWindowSize;
    private Integer fps = 0;
    private Sprite screenCover;
    private ConcurrentLinkedDeque<KeyEvent> keyboardDownEvents = new ConcurrentLinkedDeque<>();
    private ConcurrentLinkedDeque<KeyEvent> keyboardUpEvents = new ConcurrentLinkedDeque<>();

    private ConcurrentLinkedDeque<MouseEvent> mouseEvents = new ConcurrentLinkedDeque<>();

    public Application(boolean fullscreen)
    {
        isFullscreen = fullscreen;
        instance = this;
        screenCover = Sprite.square(Painter.renderWidth, Painter.renderHeight, Color.black);
    }

    public static Application current()
    {
        return instance;
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

        applicationWindow.setVisible(true);

        while (gameRunning)
        {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - previousTime;
            previousTime = currentTime;

            accumulatedTime += elapsedTime;

            if (accumulatedTime >= frameDuration)
            {
                if (!finishedFrame) continue;
                finishedFrame = false;

                loop();

                frames++;
                accumulatedTime -= frameDuration;
            }

            if (System.nanoTime() - secondTimer >= 1_000_000_000L)
            {
                fps = frames;
                frames = 0;
                secondTimer += 1_000_000_000L;
            }

            try
            {
                long sleepTime = (frameDuration - (System.nanoTime() - previousTime)) / 1_000_000;
                if (sleepTime > 0) Thread.sleep(sleepTime);
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

    private void handleInputEvents()
    {
        while(true)
        {
            KeyEvent event = keyboardDownEvents.poll();
            if(event == null)
                break;
            Input.keyboardEvent(event, true);
            if(activeScene != null)
                activeScene.handleCallbacks(event, true);
        }

        while(true)
        {
            KeyEvent event = keyboardUpEvents.poll();
            if(event == null)
                break;
            Input.keyboardEvent(event, false);
            if(activeScene != null)
                activeScene.handleCallbacks(event, false);
        }

        while(true)
        {
            MouseEvent event = mouseEvents.poll();
            if(event == null)
                break;
            Input.mouseEvent(event);
            if(activeScene != null)
                activeScene.handleCallbacks(event);
        }
    }

    private void loop()
    {
        handleInputEvents();

        boolean oHeld2 = Input.getKey(Keys.F11);
        boolean oPressed2 = Input.getKeyDown(Keys.F11);
        boolean oReleased2 = Input.getKeyUp(Keys.F11);

        forceQuitDialog();

        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
        lastFrameTime = currentTime;

        Time.update(deltaTime);

        var dialogManager = DialogBoxManager.getInstance();
        boolean hasActiveDialogBox = dialogManager.update();
        if (!hasActiveDialogBox)
            activeScene.updateScene();

        appPainter.begin();

        activeScene.drawScene(appPainter);
        dialogManager.render(appPainter);

        if (showFPS)
            appPainter.drawText(fps.toString(), new Vector2(), new Vector2(), Color.white);

        appPainter.end();

        if (Input.getKeyDown(Keys.F11))
        {
            applicationWindow.setVisible(false);
            applicationWindow.dispose();
            isFullscreen = !isFullscreen;
            prepareApplicationWindow(initialWindowSize);
            appPainter = new Painter(applicationWindow);
            applicationWindow.setVisible(true);
        }

        var bounds = applicationWindow.getBounds();
        Input.windowPosition = new Vector2(bounds.x, bounds.y);
        Input.windowSize = new Point(bounds.width, bounds.height);

        Input.update();
        finishedFrame = true;
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
            box.getTitle().setColor(Application.current().getActiveScene().getScenePallet().getColorFromIndex(6));
            DialogBoxManager.getInstance().enqueue(box);
        }
    }

    public void closeGame()
    {
        gameRunning = false;
    }

    public void loadScene(String sceneName)
    {
        loadScene(sceneName, true);
    }

    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    // suppressed to avoid redundant if statements since the while loops would act as the if statements.
    public void loadScene(String sceneName, boolean doAnimation)
    {
        Transform screenCoverPosition = new Transform();
        screenCoverPosition.setPosition(new Vector2(0, -Painter.renderHeight));
        while (doAnimation)
        {
            appPainter.begin();
            if (activeScene != null) activeScene.drawScene(appPainter);
            appPainter.drawSprite(screenCover, screenCoverPosition, new Vector2(), Color.white);
            screenCoverPosition.translateY(800 * Time.getUnscaledDeltaTime());
            appPainter.end();

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
            lastFrameTime = currentTime;

            Time.update(deltaTime);

            if (screenCoverPosition.getPosition().y > 0)
            {
                screenCoverPosition.setPosition(new Vector2());
                break;
            }
        }

        Consumer<Scene> sceneConfigurer = scenes.get(sceneName);
        if (sceneConfigurer == null)
        {
            DialogBoxManager.getInstance().enqueue("Error", "No scene with name: " + sceneName + ". Please scream at your local developer to resolve this issue!", 10);
            //throw new RuntimeException("No scene with name: " + sceneName);
        }
        else
        {
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

        while (doAnimation)
        {
            appPainter.begin();
            if (activeScene != null) activeScene.drawScene(appPainter);
            screenCoverPosition.translateY(800 * Time.getUnscaledDeltaTime());
            appPainter.drawSprite(screenCover, screenCoverPosition, new Vector2(), Color.white);
            appPainter.end();

            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0f; // Convert nanoseconds to seconds
            lastFrameTime = currentTime;

            Time.update(deltaTime);

            if (screenCoverPosition.getPosition().y > Painter.renderHeight)
            {
                screenCoverPosition.setPosition(new Vector2(0, Painter.renderHeight));
                break;
            }
        }
    }

    private void mouseEvent(MouseEvent event)
    {
        mouseEvents.push(event);
    }

    private void keyboardEvent(KeyEvent event, boolean pressed)
    {
        if (pressed)
        {
            keyboardDownEvents.push(event);
            return;
        }

        if (!pressedEventForKeyExists(event))
            keyboardUpEvents.push(event);
    }

    private boolean pressedEventForKeyExists(KeyEvent event)
    {
        var snapshot = new ArrayList<>(keyboardDownEvents);

        for (var evnt : snapshot)
        {
            if (evnt.equals(event)) return true;
            if (evnt.getKeyCode() == event.getKeyCode()) return true;
        }
        return false;
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
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e)
            {
                var event = new MouseEvent(buttonFromAWT(e), new Vector2(e.getXOnScreen(), e.getYOnScreen()), e.getClickCount() == 2);
                event.clicked = true;
                mouseEvent(event);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e)
            {
                var event = new MouseEvent(buttonFromAWT(e), new Vector2(e.getXOnScreen(), e.getYOnScreen()), e.getClickCount() == 2);
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

            private MouseButton buttonFromAWT(java.awt.event.MouseEvent e)
            {
                return switch (e.getButton())
                {
                    case java.awt.event.MouseEvent.BUTTON1 -> MouseButton.Left;
                    case java.awt.event.MouseEvent.BUTTON2 -> MouseButton.Middle;
                    case java.awt.event.MouseEvent.BUTTON3 -> MouseButton.Right;
                    default -> MouseButton.None;
                };
            }
        });

        canvas.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e)
            {
                enqueueMouseMotionEvent(e);
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e)
            {
                enqueueMouseMotionEvent(e);
            }

            private void enqueueMouseMotionEvent(java.awt.event.MouseEvent e)
            {
                var event = new MouseEvent(new Vector2(e.getXOnScreen(), e.getYOnScreen()));
                mouseEvent(event);
            }
        });

        canvas.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                keyboardEvent(e, true);
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                keyboardEvent(e, false);
            }
        });

    }


    private JFrame createCustomFrame(Point windowSize)
    {
        try
        {
            applicationWindow = new JFrame();

            applicationWindow.setIconImage(ImageIO.read( new File("resources/sprites/Appicon.png")));

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

            applicationWindow.setFocusable(true);
            applicationWindow.setFocusTraversalKeysEnabled(false);
            applicationWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            applicationWindow.getContentPane().setPreferredSize(new Dimension(width, height));
            applicationWindow.pack();
            applicationWindow.setResizable(false);

            if (!isFullscreen)
                applicationWindow.setLocation((screenWidth / 2) - (applicationWindow.getWidth() / 2), (screenHeight / 2) - (applicationWindow.getHeight() / 2));

            applicationWindow.getContentPane().setBackground(Color.BLACK);

            applicationWindow.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    if (activeScene != null)
                    {
                        if (activeScene.name.equals("LevelSelect"))
                        {
                            closeGame();
                            return;
                        }
                    }

                    ConfirmationDialogBox box = new ConfirmationDialogBox("Warning!", "Are you sure you want to force quit the game?\n" + "Any unsaved progress will be lost!", cdb -> {
                        if (cdb.getResult()) Application.current().closeGame();
                    });
                    box.getTitle().setColor(Application.current().getActiveScene().getScenePallet().getColorFromIndex(6));
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






