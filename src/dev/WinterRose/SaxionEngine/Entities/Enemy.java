package dev.WinterRose.SaxionEngine.Entities;

import BinaryBashers.EnemySprite;
import dev.WinterRose.SaxionEngine.Painter;
import dev.WinterRose.SaxionEngine.Transform;
import dev.WinterRose.SaxionEngine.Vector2;

public class Enemy
{
    private int entityID;
    private EnemySprite sprite;
    private EnemySpawner spawner;

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
        System.out.println("Enemy has been defeated.");
        spawner.killEnemy(this);
    }

    public void render(Painter painter) {
        sprite.render(painter);
    }

    public void update() {
        sprite.update();
    }

    public void startAnimation() {
        sprite.startEnteringAnimation();
    }

    public void hide() {
        sprite.hide();
    }
}