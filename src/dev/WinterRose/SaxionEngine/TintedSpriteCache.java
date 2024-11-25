package dev.WinterRose.SaxionEngine;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SpriteCache
{
    private Map<String, Sprite> cache = new HashMap<>();

    public Sprite getTintedSprite(Sprite image, Color tint)
    {
        String cacheKey = createCacheKey(image, tint);

        if(cache.containsKey(cacheKey))
            return cache.get(cacheKey);

        Sprite copy = new Sprite(image.getImage()).applyTint(tint);
        cacheKey = createCacheKey(copy, tint);
        cache.put(cacheKey, copy);
    }

    private String createCacheKey(Sprite sprite, Color tint)
    {
        return sprite.hashCode() + "_" + tint.hashCode();
    }
}
