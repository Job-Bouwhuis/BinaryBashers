package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.awt.*;

public abstract class Enemy
{
    public EnemySpawner spawner;
    private EnemySprite sprite;
    private Vector2 textPosition;
    private String text;
    private boolean isDead;
    private SoundPack deathSounds;
    public Integer decimalNum = 0;
    protected Boolean showDecimal;

    public Enemy(int spriteId, Vector2 enemyPosition, Boolean showDecimal)
    {
        this.sprite = new EnemySprite();
        this.sprite.setSpriteId(spriteId);
        sprite.transform = new Transform();
        sprite.transform.setPosition(enemyPosition); // is ugly, but it worki

        text = "...";
        textPosition = enemyPosition.subtract(new Vector2(Painter.measureString(text).x, sprite.getSolid().getHeight() + 10));
        deathSounds = new SoundPack("resources/audio/enemyDeaths");
        deathSounds.setAllVolume(0.8f);
        this.showDecimal = showDecimal;
    }

    public EnemySprite getSprite()
    {
        return sprite;
    }

    public void setText(String text)
    {
        this.text = text;
        textPosition = sprite.transform.getPosition().subtract(new Vector2(Painter.measureString(text).x / 2, sprite.getSolid().getHeight() + 10));

    }

    protected Boolean getShowDecimal()
    {
        return showDecimal;
    }


    public void kill()
    {
        isDead = true;
        sprite.showDeathAnimation();
        deathSounds.playRandom();
    }

    public void render(Painter painter)
    {
        sprite.render(painter);
        if (!isDead)
            painter.drawScaledText(text, textPosition, new Vector2(1.2f), new Vector2(0.5f, 0.5f), Color.cyan, FontType.Bold);
    }

    public void update()
    {
        sprite.update();
        if (sprite.isDeathHidden())
            spawner.killEnemy(this);
    }

    public void startAnimation()
    {
        sprite.startEnteringAnimation();
    }

    public void immediateDeath()
    {
        sprite.hide();
    }

    public abstract boolean compairInput(String input);

    void onColorPalleteChange(ColorPallet newPallet)
    {
        sprite.onColorPalleteChange(newPallet);
    }

    public abstract int getInputLength();
}