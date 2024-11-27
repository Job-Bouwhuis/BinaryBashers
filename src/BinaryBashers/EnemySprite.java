package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.Component;

import java.awt.*;

public class EnemySprite extends ActiveRenderer {

    Sprite enemySprite;
    Sprite solidEnemySprite;

    private final String BASE_SPRITE_DIR = "resources\\sprites\\enemies\\";

    boolean timerActive;
    float timer;

    public EnemySprite() {

    }

    public void setEnemyID(int id) {
        switch (id) {
            case 0:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Mathematical Mage");
                break;
            case 1:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Decimal Demon");
                break;
            case 2:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Statistical Slime");
                break;
        }
        solidEnemySprite = enemySprite.getSolid();
    }

    public void startEnteringAnimation() {
        Color tempColor1 = new Color(0, 7, 13);
        Color tempColor2 = new Color(11, 22, 42);
    }

    private void finishEnteringAnimation() {

    }

    public void hide() {

    }

    @Override
    public void render(Painter painter) {
        painter.drawSprite(sprite, transform, origin, tint);
    }

    @Override
    public void update() {
        timer =- 1 * Time.deltaTime;
    }

}
