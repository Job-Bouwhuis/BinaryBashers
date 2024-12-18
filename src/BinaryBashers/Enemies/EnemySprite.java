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

    private final float introTimerDuration = 4;
    public boolean isIntroTimerActive;
    private float introTimer;
    private float timeBetweenSprites;

    private boolean isAttackAnimationActive;
    private float attackAnimationTimer;
    private final float attackAnimationDuration = 0.15f;
    // Amount of pixels the attack animation moves in the Y axis
    private final float attackAnimationYOffset = 40;
    private Vector2 attackAnimationStartPosition;
    private Vector2 attackAnimationDestination;


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
        introTimer = 0;
        isIntroTimerActive = true;
        timeBetweenSprites = introTimerDuration / introColors.length;
        setIntoProgress(0);
        activeSprite = solidEnemySprite;

    }

    private void finishEnteringAnimation() {
        isIntroTimerActive = false;
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
        if (isIntroTimerActive && (introTimer + 1 * Time.getDeltaTime() >= introTimerDuration)) {
            finishEnteringAnimation();
        }
        else if (isIntroTimerActive) {
            updateIntroAnimation();
        }
        if (useDyingAnimation) {
            deathAnimationSprite.update();
        }
        if (isAttackAnimationActive) {
            updateAttackAnimation();
        }
    }

    private void updateIntroAnimation() {
        introTimer += 1 * Time.getDeltaTime();
        int animationProgress = (int) Math.floor(introTimer / timeBetweenSprites);
        setIntoProgress(animationProgress);
    }

    @Override
    public void onColorPalleteChange(ColorPallet colorPallet) {
        introColors = new Color[] {
                colorPallet.getColorFromIndex(0),
                colorPallet.getColorFromIndex(1),
                colorPallet.getColorFromIndex(2)
        };

        enemySprite = SpritePalletChanger.changePallet(enemySprite, colorPallet);
        deathAnimationSprite.onColorPalleteChange(colorPallet);
    };

    public Sprite getSolid() {
        return solidEnemySprite;
    }

    public void startAttackAnimation() {
        System.out.println("test1");
        attackAnimationStartPosition = transform.getPosition();
        attackAnimationDestination = attackAnimationStartPosition.add(new Vector2(0,attackAnimationYOffset));
        isAttackAnimationActive = true;
        attackAnimationTimer = 0;
    }

    private void updateAttackAnimation() {
        attackAnimationTimer += Time.getDeltaTime();
        float animationProgress = attackAnimationTimer / (attackAnimationDuration / 2);
        if (animationProgress < 1) {
            transform.setPosition(attackAnimationStartPosition.lerp(attackAnimationDestination, animationProgress));
        }
        else {
            transform.setPosition(attackAnimationDestination.lerp(attackAnimationStartPosition, animationProgress - 1));

        }
        if (attackAnimationTimer >= attackAnimationDuration) {
            isAttackAnimationActive = false;
        }
    }
}
