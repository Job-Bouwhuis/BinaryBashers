package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;
import dev.WinterRose.SaxionEngine.ColorPallets.SpritePalletChanger;

import java.awt.*;

public class EnemySprite extends ActiveRenderer {

    private Sprite enemySprite;
    private Sprite solidEnemySprite;
    private Sprite activeSprite;
    private Color solidColorTint;

    private final String BASE_SPRITE_DIR = "resources/sprites/enemies/";

    private final float timerDuration = 4;
    public boolean timerActive;
    private float timer;
    private float timeBetweenSprites;

    public boolean hidden = true;
    private boolean useDyingAnimation;

    // TODO: Rework into ColorPallete type
    private final Color[] defaultIntroColors = {
            new Color(0, 7, 13),
            new Color(11, 22, 42),
            new Color(31, 44, 61),
    };
    private Color[] introColors = defaultIntroColors;

    private AnimatedSpriteRenderer deathAnimationSprite = new AnimatedSpriteRenderer(new Sprite[]{
            new Sprite("resources/sprites/enemyDeath/Explosion1.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion2.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion3.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion4.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion5.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion6.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion7.png"),
            new Sprite("resources/sprites/enemyDeath/Explosion8.png"),
    },0.1f, false);

    public boolean isDeathHidden()
    {
        return useDyingAnimation && deathAnimationSprite.isHidden();
    }

    public void setSpriteId(int id) {
        switch (id) {
            case 0:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "MathematicalMage.png");
                break;
            case 1:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "DecimalDemon.png");
                break;
            case 2:
                enemySprite = new Sprite(BASE_SPRITE_DIR + "StatisticalSlime.png");
                break;
        }
        solidEnemySprite = enemySprite.getSolid();
    }

    private void setIntoProgress(int progress) {
        solidColorTint = introColors[progress];
    }

    public void startEnteringAnimation() {
        hidden = false;
        useDyingAnimation = false;
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

    public void showDeathAnimation() {
        useDyingAnimation = true;
        deathAnimationSprite.hideOnEnd = true;
    }

    @Override
    public void render(Painter painter) {
        if (!hidden && !useDyingAnimation) {
            painter.drawSprite(activeSprite, transform, new Vector2(0.5f, 0.5f), solidColorTint);
        }
        if (useDyingAnimation) {
            deathAnimationSprite.manualRender(painter, transform);
        }
    }

    @Override
    public void update() {
        if (timerActive && (timer + 1 * Time.deltaTime >= timerDuration)) {
            finishEnteringAnimation();
        }
        else if (timerActive) {
            updateIntroAnimation();
        }
        if (useDyingAnimation) {
            deathAnimationSprite.update();
        }
    }

    private void updateIntroAnimation() {
        timer += 1 * Time.deltaTime;
        int animationProgress = (int) Math.floor(timer / timeBetweenSprites);
        setIntoProgress(animationProgress);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet) {
        introColors = new Color[] {
                colorPallet.getColorFromIndex(0),
                colorPallet.getColorFromIndex(1),
                colorPallet.getColorFromIndex(2)
        };

        if(timerActive)
            enemySprite = SpritePalletChanger.changePallet(enemySprite, colorPallet);
        else
            activeSprite = SpritePalletChanger.changePallet(activeSprite, colorPallet);
        deathAnimationSprite.onColorPalleteChange(colorPallet);
    };

    public Sprite getSolid()
    {
        return solidEnemySprite;
    }

}
