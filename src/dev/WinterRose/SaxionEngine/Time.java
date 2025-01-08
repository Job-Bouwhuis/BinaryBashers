package dev.WinterRose.SaxionEngine;

public class Time
{
    private static float deltaTime = 0;

    private static float timeScale = 1;

    public static float getDeltaTime()
    {
        return deltaTime * timeScale;
    }

    public static void setTimeScale(float scale)
    {
        timeScale = scale;
    }

    public static float getTimeScale()
    {
        return timeScale;
    }

    static void update(float deltaTime)
    {
        Time.deltaTime = deltaTime;
    }

    public static float getUnscaledDeltaTime()
    {
        return deltaTime;
    }
}
