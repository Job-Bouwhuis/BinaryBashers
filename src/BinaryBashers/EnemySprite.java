package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;

import java.awt.*;

public class EnemySprite extends ActiveRenderer {

    private Sprite enemySprite;
    private Sprite solidEnemySprite;
    private Sprite activeSprite;
    private Color solidColorTint;

    private final String BASE_SPRITE_DIR = "resources\\sprites\\enemies\\";

    private final float timerDuration = 4;
    public boolean timerActive;
    private float timer;
    private float timeBetweenSprites;

    private boolean hidden = true;

    // TODO: Rework into ColorPallete type
    private final Color[] introColors = {
            new Color(0, 7, 13),
            new Color(11, 22, 42),
            new Color(31, 44, 61),
    };

    public void setSpriteId(int id) {
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

    private void setIntoProgress(int progress) {
        if (progress < introColors.length)
            solidColorTint = introColors[progress];
    }

    public void startEnteringAnimation() {
        hidden = false;
        timer = 0;
        timerActive = true;
        timeBetweenSprites = timerDuration / introColors.length;
        setIntoProgress(0);
        activeSprite = solidEnemySprite;

    }

    private void finishEnteringAnimation() {
        timerActive = false;
        solidColorTint = Color.white;
        activeSprite = enemySprite;
    }

    public void hide() {
        hidden = true;
    }

    @Override
    public void render(Painter painter) {
        if (!hidden) {
            painter.drawSprite(activeSprite, transform, new Vector2(0.5f, 0.5f), solidColorTint);
        }
    }

    @Override
    public void update() {
        if (timerActive && timer >= timerDuration) {
            finishEnteringAnimation();
        }
        else if (timerActive) {

        }
    }

    private void updateIntroAnimation() {
        timer += 1 * Time.deltaTime;
        // Something is not correct in this calculation. The first color is on screen for less frames.
        // I may be stupid
        int animationProgress = (int) Math.floor(timer / timeBetweenSprites);
        setIntoProgress(animationProgress);
    }

}
