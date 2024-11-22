package dev.WinterRose.SaxionEngine;

import nl.saxion.app.interaction.GameLoop;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

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
    public void mouseEvent(MouseEvent mouseEvent)
    {
        Input.mouseEvent(mouseEvent);
        activeScene.handleCallbacks(mouseEvent);
    }
}






