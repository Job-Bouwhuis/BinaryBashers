package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Timer extends ActiveRenderer
{
    private float currentTime;
    private float maxTime;
    private float speedMultiplier = 1;
    public Action<Timer> onTimeAction = new Action<>();
    private boolean isRunning;
    private boolean autoLoop;
    Sprite[] timerSprites = {
            new Sprite("resources/sprites/ui/timer/Timer1.png"),
            new Sprite("resources/sprites/ui/timer/Timer2.png"),
            new Sprite("resources/sprites/ui/timer/Timer3.png"),
            new Sprite("resources/sprites/ui/timer/Timer4.png")
    };
    private boolean paused;

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
        speedMultiplier = amount;
    }

    public float getSpeedMultiplier()
    {
        return speedMultiplier;
    }

    public void setMaxTime(float time)
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

    public void skipTo(float time){
        currentTime = time;
        currentTime = Math.clamp(currentTime, 0f, maxTime);
    }

    private Sprite selectTimerSprite()
    {
        float percent = currentTime / maxTime;
        return timerSprites[(int)(percent*timerSprites.length)];
    }

    @Override
    public void render(Painter painter)
    {
        if(timerSprites.length == 0)
            return;
        Sprite current = selectTimerSprite();
        painter.drawSprite(current, transform, new Vector2(.5f, .5f), Color.white);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet)
    {
        super.onColorPalleteChange(colorPallet);
        for (int i = 0; i < timerSprites.length; i++)
        {
            timerSprites[i] = SpritePalletChanger.changePallet(timerSprites[i], colorPallet);
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void stop()
    {
        isRunning = false;
    }

    public void setSprites(Sprite[] sprites)
    {
        timerSprites = sprites;
    }
}
