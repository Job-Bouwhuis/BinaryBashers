package dev.WinterRose.SaxionEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Timer extends Behavior
{
    private float currentTime;
    private float maxTime;
    private float speedMultiplier;
    public Action<Timer> onTimeAction = new Action<>();
    private boolean isRunning;
    private boolean autoLoop;

    @Override
    public void update()
    {
        if (isRunning)
        {
            currentTime += Time.deltaTime * speedMultiplier;
            if (currentTime >= maxTime)
            {
                onTimeAction.invoke(this);
                if (autoLoop)
                {
                    restart();
                }
            }
        }
    }

    //automatic loops and or start
    public Timer(float maxTime, boolean autoLoop, boolean autoStart, float speedMultiplier)
    {
        this.maxTime = maxTime;
        this.autoLoop = autoLoop;
        this.speedMultiplier = speedMultiplier;
        if (autoStart)
        {
            restart();
        }
    }

    public void setSpeedMultiplier(float amount)
    {
        speedMultiplier += amount;
    }

    public float getSpeedMultiplier()
    {
        return speedMultiplier;
    }

    public void SetMaxTime(float time)
    {
        maxTime = time;
    }

    public float getMaxTime()
    {
        return maxTime;
    }

    public void setAutoLoop(boolean autoLoop)
    {
        this.autoLoop = autoLoop;
    }

    public boolean getAutoLoop()
    {
        return autoLoop;
    }

    public float getCurrentTime()
    {
        return currentTime;
    }

    public void start()
    {
        isRunning = true;
    }

    public void restart()
    {
        currentTime = 0;
        start();
    }
}
