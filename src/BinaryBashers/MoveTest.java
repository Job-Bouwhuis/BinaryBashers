package BinaryBashers;

import dev.WinterRose.SaxionEngine.*;

public class MoveTest extends Behavior
{
    float speed = 200f;

    @Override
    public void update()
    {
        Vector2 moveVec = new Vector2();
        if (Input.getKey(Keys.W)) moveVec.y = -1;
        if (Input.getKey(Keys.A)) moveVec.x = -1;
        if (Input.getKey(Keys.D)) moveVec.x = 1;
        if (Input.getKey(Keys.S)) moveVec.y = 1;

        moveVec.normalize().multiply(Time.deltaTime).multiply(speed);
        transform.getPosition().add(moveVec);

        if (Input.getKey(Keys.E))
            transform.getRotation().add(1);
        if (Input.getKey(Keys.Q))
            transform.getRotation().subtract(1);

        if (Input.getKey(Keys.T))
            transform.getScale().add(new Vector2(1, 1).multiply(Time.deltaTime));
        if (Input.getKey(Keys.G))
            transform.getScale().subtract(new Vector2(1, 1).multiply(Time.deltaTime));
    }
}
