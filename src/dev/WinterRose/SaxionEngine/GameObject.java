package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;
import dev.WinterRose.SaxionEngine.Callbacks.IMouseCallback;
import nl.saxion.app.interaction.KeyboardEvent;
import nl.saxion.app.interaction.MouseEvent;

import java.util.ArrayList;

public class GameObject
{
    public String name;
    public String flag;

    ArrayList<Component> components; // these hold just data, nothing more
    ArrayList<Behavior> behaviors; // these have an update method
    ArrayList<Renderer> renderers; // these have a render method, but no update method

    ArrayList<IKeystrokeCallback> keystrokeCallbacks; // these components want to know when any key is pressed on the keyboard
    ArrayList<IMouseCallback> mouseCallbacks; // these components want to know when mouse information changes

    public final Transform transform;
    Scene scene;

    public GameObject(String name)
    {
        components = new ArrayList<>();
        behaviors = new ArrayList<>();
        renderers = new ArrayList<>();
        keystrokeCallbacks = new ArrayList<>();
        mouseCallbacks = new ArrayList<>();
        transform = new Transform();
        components.add(transform);
        this.name = name;
    }

    /**
     * Adds the given component to the object and sets the transform and owner reference within this component instane. Adding an instance of Transform is ignored.
     * @param component
     */
    public void addComponent(Component component)
    {
        if(component instanceof Transform)
            return; // ignore transform additions to this object. that is illegal

        component.transform = transform;
        component.owner = this;

        if(component instanceof Behavior behavior)
            behaviors.add(behavior);

        if(component instanceof Renderer renderer)
            renderers.add(renderer);

        if(component instanceof IKeystrokeCallback callback)
            keystrokeCallbacks.add(callback);

        if(component instanceof IMouseCallback callback)
            mouseCallbacks.add(callback);

        components.add(component);
    }

    /**
     * Gets the scene this object is in
     */
    public Scene GetScene()
    {
        return scene;
    }

    void handleKeystrokeCallbacks(KeyboardEvent eventArgs)
    {
        for(IKeystrokeCallback callback : keystrokeCallbacks)
            callback.keyPress(eventArgs);
    }

    void handleMouseCallbacks(MouseEvent eventArgs)
    {
        for (IMouseCallback callback : mouseCallbacks)
            callback.mouseUpdate(eventArgs);
    }

    void updateObject()
    {
        for(var behavior : behaviors)
        {
            behavior.update();
        }
    }

    void drawObject(Painter painter)
    {
        for(var renderer : renderers)
        {
            renderer.render(painter);
        }
    }

    public void wakeObject() {
        for(var comp : components)
        {
            comp.awake();
        }
    }

    public void destroyed()
    {
        for(var comp : components)
        {
            comp.onDestroyed();
        }
    }
}
