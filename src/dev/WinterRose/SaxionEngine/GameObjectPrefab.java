package dev.WinterRose.SaxionEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameObjectPrefab
{
    private static Map<String, Consumer<GameObject>> objectPrefabs = new HashMap<>();

    public static void addPrefab(String name, Consumer<GameObject> configurer)
    {
        objectPrefabs.put(name, configurer);
    }

    public static GameObject instantiate(String name, Scene scene)
    {
        Consumer<GameObject> configurer = objectPrefabs.get(name);

        if(configurer == null)
            throw new IllegalArgumentException("object with name '%s' not found as prefab.");

        GameObject obj = new GameObject(name);
        configurer.accept(obj);
        scene.addObject(obj);
        return obj;
    }
}
