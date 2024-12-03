package dev.WinterRose.SaxionEngine.Entities;

import BinaryBashers.EnemySprite;
import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.Transform;
import dev.WinterRose.SaxionEngine.Vector2;

public abstract class Enemy
{
    private int entityID;
    private EnemySprite sprite;
    public EnemySpawner spawner;
    private boolean isDead;

    public Enemy(int id, Vector2 enemyPosition) {
        this.entityID = id;
        this.sprite = new EnemySprite();
        this.sprite.setSpriteId(id);
        sprite.transform = new Transform();
        sprite.transform.setPosition(enemyPosition); // is ugly, but it worki
    }

    public EnemySprite getSprite() {
        return sprite;
    }

    public void death() {
        isDead = true;
        sprite.showDeathAnimation();
    }

    public void render(Painter painter) {
        sprite.render(painter);
    }

    public void update() {
        sprite.update();
        if(sprite.hidden)
        {
            spawner.killEnemy(this);
        }
    }

    public void startAnimation() {
        sprite.startEnteringAnimation();
    }

    public void hide() {
        sprite.hide();
    }

    public abstract boolean compairInput(String input);
}