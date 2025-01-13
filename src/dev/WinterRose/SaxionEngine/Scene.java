package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.DialogBoxes.DialogBoxManager;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Scene
{
    public final String name;
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private final ArrayList<GameObject> objectsToDestroy = new ArrayList<>();
    boolean initialized;
    private ColorPallet scenePallet = null;

    public Scene(String name)
    {
        this.name = name;
    }

    public void wakeScene()
    {
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            try
            {
                GameObject obj = objects.get(i);
                obj.wakeObject();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                DialogBoxManager.getInstance()
                        .enqueue("Fatal error on object Awake", e.getMessage() + "\n\nPlease notify the developers to fix this issue!\nThe game will close after closing this dialog.", box -> {
                            Application.current().closeGame();
                        });
            }
        }
        initialized = true;
    }

    public void updateScene()
    {
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            try
            {
                var obj = objects.get(i);
                if (obj.isActive) obj.updateObject();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                DialogBoxManager.getInstance()
                        .enqueue("Fatal error on object Update", e.getMessage() + "\n\nPlease notify the developers to fix this issue!\nThe game will close after closing this dialog.", box -> {
                            Application.current().closeGame();
                        });
            }
        }

        for (var obj : objectsToDestroy)
        {
            if (!objects.contains(obj)) continue;
            obj.onDestroyInternal();
            objects.remove(obj);
        }
        objectsToDestroy.clear();
    }

    public void drawScene(Painter appPainter)
    {
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            try
            {
                var obj = objects.get(i);
                if (obj.isActive) obj.drawObject(appPainter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                DialogBoxManager.getInstance()
                        .enqueue("Fatal error on object Draw", e.getMessage() + "\n\nPlease notify the developers to fix this issue!\nThe game will close after closing this dialog.", box -> {
                            Application.current().closeGame();
                        });
            }
        }
    }

    public void addObject(GameObject obj)
    {
        objects.add(obj);
        obj.scene = this;
        if (initialized) obj.wakeObject();
    }

    public GameObject createObject(String name)
    {
        GameObject newObj = new GameObject(name);
        addObject(newObj);
        return newObj;
    }

    public void handleCallbacks(KeyEvent e, boolean pressed)
    {
        for (var obj : objects)
            obj.handleKeystrokeCallbacks(e, pressed);
    }

    public void handleCallbacks(MouseEvent mouseEvent)
    {
        for (var obj : objects)
            obj.handleMouseCallbacks(mouseEvent);
    }

    public void destroy(GameObject gameObject)
    {
        objectsToDestroy.add(gameObject);
    }

    public void destroyImmediate(GameObject gameObject)
    {
        if (objects.contains(gameObject))
        {
            gameObject.onDestroyInternal();
            objects.remove(gameObject);
        }
    }

    public boolean checkIfPalletExists()
    {
        return scenePallet != null;
    }

    public ColorPallet getScenePallet()
    {
        if (scenePallet == null) return new ColorPallet();
        return scenePallet;
    }

    public void setScenePallet(ColorPallet newPallet)
    {
        scenePallet = newPallet;
        for (GameObject object : objects)
        {
            object.updatePallete(newPallet);
        }
    }
}

