package BinaryBashers.Enemies;

import BinaryBashers.UI.DialogBoxes.ConfirmationDialogBox;
import BinaryBashers.UI.DialogBoxes.DialogBoxManager;
import dev.WinterRose.SaxionEngine.Application;
import dev.WinterRose.SaxionEngine.Component;
import dev.WinterRose.SaxionEngine.Timer;

public class Player extends Component
{

    private int health = 3;
    private EnemySpawner spawner;

    private Timer damageTimer;

    private int score = 0; // NOTE: has to be moved to scoreboard eventually

    public Player(int health, EnemySpawner<?> spawner)
    {
        this.health = health;

        this.spawner = spawner;
        spawner.onEnemyCountChanged.add(newEnemyCount -> {

            if(newEnemyCount == 0)
            {
                damageTimer.stop();
            }
            else if (!damageTimer.isRunning())
            {
                damageTimer.restart();
            }

            damageTimer.setSpeedMultiplier(switch (newEnemyCount)
            {
                case 1 -> 1.2f;
                case 2 -> 1.4f;
                case 3 -> 1.6f;
                default -> 1;
            });
        });
    }

    @Override
    public void awake()
    {
        damageTimer = owner.getComponent(Timer.class);
        damageTimer.onTimeAction.add(t -> takeDamage(1));
        damageTimer.stop();
    }

    public int getHealth()
    {
        return health;
    }

    public void takeDamage(int damage)
    {
        if (spawner.hasEnemies())
        {
            health -= damage;
            System.out.println("Player takes " + damage + " damage. Remaining health: " + health);
            if (health <= 0)
            {
                death();
            }
        }
    }

    public void death()
    {
        DialogBoxManager.getInstance().enqueue(new ConfirmationDialogBox("DEDE", "You died. Score: -1", confirmationDialogBox -> {
            Application.getInstance().closeGame();
        }));
    }
}
