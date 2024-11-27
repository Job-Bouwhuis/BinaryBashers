public class Player extends Entity {
    private int health = 3;

    public Player(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println("Player takes " + damage + " damage. Remaining health: " + health);
        if (health <= 0) {
            death();
        }
    }

    @Override public void death() {
        System.out.println("Player has died. Game Over.");
    }
}
