package dev.WinterRose.SaxionEngine;

import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.util.ArrayList;

public class Scene
{
    public String name;
    ArrayList<GameObject> objects = new ArrayList<>();
    Painter scenePainter;

    public Scene(String name)
    {
        this.name = name;
        scenePainter = new Painter();
    }

    public void wakeScene()
    {
        for (var obj : objects)
            obj.wakeObject();
    }

    public void updateScene()
    {
        for (var obj : objects)
            obj.updateObject();
    }

    public void drawScene()
    {

        scenePainter.begin();
        for (var obj : objects)
            obj.drawObject(scenePainter);

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
}

