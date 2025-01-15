package dev.WinterRose.SaxionEngine;

public class Time
{
    private static float deltaTime = 0;
    private static float timeScale = 1;
    private static float sinceStartup = 0;

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
        Time.sinceStartup += deltaTime;
    }

    public static float getUnscaledDeltaTime()
    {
        return deltaTime;
    }

    /**
     * @return The time in miliseconds since the game started
     */
    public static float getSinceStartup()
    {
        return sinceStartup;
    }
}
