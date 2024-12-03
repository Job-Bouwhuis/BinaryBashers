package BinaryBashers.Enemies;

import BinaryBashers.EnemySprite;
import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

import java.awt.*;

public abstract class Enemy
{
    public EnemySpawner spawner;
    private EnemySprite sprite;
    private final Vector2 textPosition;
    protected String text;
    private boolean isDead;

    public Enemy(int spriteId, Vector2 enemyPosition)
    {
        this.sprite = new EnemySprite();
        this.sprite.setSpriteId(spriteId);
        sprite.transform = new Transform();
        sprite.transform.setPosition(enemyPosition); // is ugly, but it worki

        text = "...";
        textPosition = enemyPosition.subtract(new Vector2(0, sprite.getSolid().getHeight() + 10));
    }

    public EnemySprite getSprite()
    {
        return sprite;
    }

    public void death()
    {
        isDead = true;
        sprite.showDeathAnimation();
    }

    public void render(Painter painter)
    {
        sprite.render(painter);
        if(!isDead)
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
}