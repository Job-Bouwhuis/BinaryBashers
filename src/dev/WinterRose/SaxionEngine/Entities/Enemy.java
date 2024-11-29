package dev.WinterRose.SaxionEngine.Entities;

import BinaryBashers.EnemySprite;
import dev.WinterRose.SaxionEngine.Painter;

public class Enemy extends Entity {
    private int entityID;
    private EnemySprite sprite;
    private EnemySpawner spawner;

    public Enemy(int id) {
        this.entityID = id;
        this.sprite = new EnemySprite();
        this.sprite.setSpriteId(id);
    }

    public EnemySprite getSprite() {
        return sprite;
    }

    @Override
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