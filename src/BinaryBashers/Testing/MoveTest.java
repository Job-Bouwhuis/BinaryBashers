package BinaryBashers.Testing;

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

        moveVec.normalize().multiply(Time.getDeltaTime()).multiply(speed);
        transform.translateXY(moveVec);

        if (Input.getKey(Keys.E))
            transform.addRotation(1);
        if (Input.getKey(Keys.Q))
            transform.addRotation(-1);

        if (Input.getKey(Keys.T))
            transform.getScale().add(new Vector2(1, 1).multiply(Time.getDeltaTime()));
        if (Input.getKey(Keys.G))
            transform.getScale().subtract(new Vector2(1, 1).multiply(Time.getDeltaTime()));
    }
}
