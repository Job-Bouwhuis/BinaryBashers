package BinaryBashers.Enemies;

import dev.WinterRose.SaxionEngine.*;
import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

public abstract class Enemy
{
    private final Vector2 enemyPosition;
    public EnemySpawner spawner;
    private EnemySprite sprite;
    private Vector2 textPosition;
    private String text;
    private boolean isDead;
    private SoundPack deathSounds;
    public Integer decimalNum = 0;
    protected Boolean showDecimal;
    private long spawnTime;

    public Enemy(int spriteId, Vector2 enemyPosition, Boolean showDecimal)
    {
        this.enemyPosition = enemyPosition;
        this.sprite = new EnemySprite();
        this.sprite.setSpriteId(spriteId);
        sprite.transform = new Transform();
        sprite.transform.setPosition(enemyPosition); // is ugly, but it worki

        text = "...";
        textPosition = enemyPosition.subtract(new Vector2(Painter.measureString(text).x, sprite.getSolid().getHeight() + 10));
        deathSounds = new SoundPack("resources/audio/enemyDeaths");
        deathSounds.setAllVolume(0.8f);
        this.showDecimal = showDecimal;
        this.spawnTime = System.currentTimeMillis();
    }

    protected void setText(String text)
    {
        this.text = getFormatString(getDisplayFormat()) + text + " as " + getInputFormat();
        textPosition = enemyPosition.subtract(new Vector2(0, sprite.getSolid().getHeight() + 5));
        if(enemyPosition.equals(Painter.renderCenter)) // if the enemy is the center enemy, then move its text down so that it cant overlap with enemies on its left or right
            textPosition = textPosition.add(new Vector2(0, 25));

        Vector2 size = Painter.measureString(this.text);
        textPosition = textPosition.subtract(new Vector2(size.x / 2, 0));
    }

    public int getTimeTaken() {
        return (int) ((System.currentTimeMillis() - spawnTime) / 1000); // Time in seconds
    }

    public abstract EnemyFormat getDisplayFormat();
    public abstract EnemyFormat getInputFormat();

    public abstract String problem();
    public abstract String answer();

    public EnemySprite getSprite()
    {
        return sprite;
    }

    protected Boolean getShowDecimal()
    {
        return showDecimal;
    }

    private String getFormatString(EnemyFormat format)
    {
        return switch(format)
        {
            case Decimal -> "";
            case Binary -> "0b";
            case Hex -> "0x";
        };
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
        if(!isDead)
            painter.drawScaledText(text, textPosition, new Vector2(1.2f), new Vector2(0.5f, 0.5f), Application.current().getActiveScene().getScenePallet().getColorFromIndex(6), FontType.Bold);
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