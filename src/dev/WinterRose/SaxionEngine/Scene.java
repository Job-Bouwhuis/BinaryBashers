package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import nl.saxion.app.interaction.KeyboardEvent;

import java.util.ArrayList;

public class Scene
{
    public final String name;
    private final ArrayList<GameObject> objects = new ArrayList<>();
    private final ArrayList<GameObject> objectsToDestroy = new ArrayList<>();
    private Painter scenePainter;
    private ColorPallet scenePallet = null;
    boolean initialized;

    public Scene(String name)
    {
        this.name = name;
        scenePainter = new Painter();
    }

    public void wakeScene()
    {
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            GameObject obj = objects.get(i);
            obj.wakeObject();
        }
        initialized = true;
    }

    public void updateScene()
    {
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            var obj = objects.get(i);
            if (obj.isActive) obj.updateObject();
        }

        for(var obj : objectsToDestroy)
        {
            if(!objects.contains(obj))
                continue;
            obj.onDestroyInternal();
            objects.remove(obj);
        }
        objectsToDestroy.clear();
    }

    public void drawScene()
    {
        scenePainter.begin();
        for (int i = 0, objectsSize = objects.size(); i < objectsSize; i++)
        {
            var obj = objects.get(i);
            if (obj.isActive) obj.drawObject(scenePainter);
        }

        nl.saxion.app.SaxionApp.clear();
        scenePainter.end();
    }

    public void addObject(GameObject obj)
    {
        objects.add(obj);
        obj.scene = this;
        if(initialized)
            obj.wakeObject();
    }

    public void handleCallbacks(KeyboardEvent e)
    {
        for (var obj : objects)
            obj.handleKeystrokeCallbacks(e);
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
        if(objects.contains(gameObject))
        {
            gameObject.onDestroyInternal();
            objects.remove(gameObject);
        }
    }

    public void setScenePallet(ColorPallet newPallet) {
        scenePallet = newPallet;
        for (GameObject object : objects) {
            object.updatePallete(newPallet);
        }
    }
}

