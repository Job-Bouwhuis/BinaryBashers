package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;
import nl.saxion.app.SaxionApp;

import java.awt.*;

public class EnemySprite extends ActiveRenderer {

    private Sprite enemySprite;
    private Sprite solidEnemySprite;
    private Sprite activeSprite;
    private Color solidColorTint;

    private final String BASE_SPRITE_DIR = "resources\\sprites\\enemies\\";

    private final float timerDuration = 10;
    public boolean timerActive;
    private float timer;
    private float timeBetweenSprites;

    // TODO: Rework into ColorPallete type
    private final Color[] introColors = {
            new Color(0, 7, 13),
            new Color(11, 22, 42),
            new Color(31, 44, 61),
    };

    public void SetSpriteId(int id) {
        switch (id) {
            case 0:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Mathematical Mage.png");
                break;
            case 1:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Decimal Demon.png");
                break;
            case 2:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "Statistical Slime.png");
                break;
        }
        solidEnemySprite = enemySprite.getSolid();
    }

    private void SetIntoProgress(int progress) {
        solidColorTint = introColors[progress];
    }

    public void startEnteringAnimation() {
        timer = 0;
        timerActive = true;
        timeBetweenSprites = timerDuration / introColors.length;
        SetIntoProgress(0);
        activeSprite = solidEnemySprite;

    }

    private void finishEnteringAnimation() {

    }

    public void hide() {

    }

    @Override
    public void render(Painter painter) {
        painter.drawSprite(activeSprite, transform, new Vector2(0.5f, 0.5f), solidColorTint);
    }

    @Override
    public void update() {
        if (timerActive && timer >= timerDuration) {
            timerActive = false;
            solidColorTint = Color.white;
            activeSprite = enemySprite;
        }
        if (timerActive) {
            timer += 1 * Time.deltaTime;
            int animationProgress = (int) Math.floor(timer / timeBetweenSprites);
            System.out.println(Math.floor(timer / timeBetweenSprites));
            SetIntoProgress(animationProgress);
        }
    }

}
