package dev.WinterRose.SaxionEngine;

import nl.saxion.app.interaction.KeyboardEvent;

import java.util.ArrayList;

public class Scene
{
    public String name;
    ArrayList<GameObject> objects = new ArrayList<>();
    private ArrayList<GameObject> objectsToDestroy = new ArrayList<>();
    Painter scenePainter;

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
}

