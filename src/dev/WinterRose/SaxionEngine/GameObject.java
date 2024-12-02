package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.Callbacks.IKeystrokeCallback;
import dev.WinterRose.SaxionEngine.Callbacks.IMouseCallback;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import nl.saxion.app.interaction.KeyboardEvent;

import java.util.ArrayList;

public class GameObject
{
    public final Transform transform;
    public String name;
    public String flag;
    public boolean isActive = true;
    public boolean isDestroyed = false;

    private ArrayList<Component> components; // these hold just data, nothing more
    private ArrayList<Behavior> behaviors; // these have an update method
    private ArrayList<Renderer> renderers; // these have a render method, but no update method
    private ArrayList<ActiveRenderer> activeRenderers; // renderers that both have a render method and a update method
    private ArrayList<IKeystrokeCallback> keystrokeCallbacks; // these components want to know when any key is pressed on the keyboard
    private ArrayList<IMouseCallback> mouseCallbacks; // these components want to know when mouse information changes
    /*internal*/ Scene scene;

    public GameObject(String name)
    {
        components = new ArrayList<>();
        behaviors = new ArrayList<>();
        renderers = new ArrayList<>();
        activeRenderers = new ArrayList<>();
        keystrokeCallbacks = new ArrayList<>();
        mouseCallbacks = new ArrayList<>();
        transform = new Transform();
        transform.owner = this;
        components.add(transform);
        this.name = name;
    }

    /**
     * Adds the given component to the object and sets the transform and owner reference within this component instane. Adding an instance of Transform is ignored.
     * @param component
     */
    public void addComponent(Component component)
    {
        assertDestroyed();
        if (component instanceof Transform) return; // ignore transform additions to this object. that is illegal

        component.transform = transform;
        component.owner = this;

        if (component instanceof Behavior behavior) behaviors.add(behavior);

        if (component instanceof Renderer renderer) renderers.add(renderer);

        if (component instanceof IKeystrokeCallback callback) keystrokeCallbacks.add(callback);

        if (component instanceof IMouseCallback callback) mouseCallbacks.add(callback);

        if (component instanceof ActiveRenderer renderer) activeRenderers.add(renderer);

        components.add(component);
    }

    public <T> T getComponent(Class<T> componentType)
    {
        assertDestroyed();
        for (int i = 0; i < components.size(); i++)
        {
            var comp = components.get(i);
            if (componentType.isInstance(comp)) // << i hate java generics. C# generics are far superior... -job
                return (T) comp;
        }

        return null; // no component of type T was found.
    }

    public <T> ArrayList<T> getComponents(Class<T> componentType)
    {
        assertDestroyed();
        ArrayList<T> foundComponents = new ArrayList<>();
        for (int i = 0; i < components.size(); i++)
        {
            var comp = components.get(i);
            if (componentType.isInstance(comp)) foundComponents.add((T) comp);
        }

        return foundComponents;
    }

    /**
     * removes all instances of the component of type T
     * @param componentType
     * @return
     * @param <T>
     */
    public <T> void removeComponent(Class<T> componentType)
    {
        assertDestroyed();
        for (int i = components.size() - 1; i >= 0; i--)
            if (componentType.isInstance(components.get(i))) components.remove(i);
    }

    public <T> boolean hasComponent(Class<T> componentType)
    {
        for (int i = 0; i < components.size(); i++)
        {
            var comp = components.get(i);
            if (componentType.isInstance(comp)) return true;
        }
        return false;
    }

    /**
     * Gets the scene this object is in
     */
    public Scene getScene()
    {
        assertDestroyed();
        return scene;
    }

    void handleKeystrokeCallbacks(KeyboardEvent eventArgs)
    {
        for (int i = 0; i < keystrokeCallbacks.size(); i++)
        {
            IKeystrokeCallback callback = keystrokeCallbacks.get(i);
            callback.keyPress(eventArgs);
        }
    }

    void handleMouseCallbacks(MouseEvent eventArgs)
    {
        for (int i = 0; i < mouseCallbacks.size(); i++)
        {
            IMouseCallback callback = mouseCallbacks.get(i);
            callback.mouseUpdate(eventArgs);
        }
    }

    void updateObject()
    {
        assertDestroyed();
        for (int i = 0; i < behaviors.size(); i++)
        {
            var behavior = behaviors.get(i);
            behavior.update();
        }
    }

    void drawObject(Painter painter)
    {
        assertDestroyed();
        for (int i = 0; i < renderers.size(); i++)
        {
            var renderer = renderers.get(i);
            renderer.render(painter);
        }
        for (int i = 0; i < activeRenderers.size(); i++)
        {
            var renderer = activeRenderers.get(i);
            renderer.render(painter);
        }
    }

    public void wakeObject()
    {
        assertDestroyed();
        for (int i = 0; i < components.size(); i++)
        {
            var comp = components.get(i);
            comp.awake();
        }
    }

    public void destroy()
    {
        assertDestroyed();
        scene.destroy(this);
    }

    public void destroyImmediate()
    {
        assertDestroyed();
        scene.destroyImmediate(this);
    }

    void onDestroyInternal()
    {
        assertDestroyed();
        for (int i = 0; i < components.size(); i++)
        {
            var comp = components.get(i);
            comp.onDestroyed();
        }
    }

    private void assertDestroyed()
    {
        if(isDestroyed)
            throw new IllegalStateException("Object %s is destroyed, while trying to access it");
    }

    public void updatePallete(ColorPallet scenePallet) {
        for (Component component : components) {
            component.onColorPalleteChange(scenePallet);
        }
    }
}
