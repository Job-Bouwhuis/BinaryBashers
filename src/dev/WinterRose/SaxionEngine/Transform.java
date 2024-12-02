package dev.WinterRose.SaxionEngine;

import java.util.ArrayList;

public class Transform extends Component
{
    private Vector2 position;
    private float rotation;
    private Vector2 scale;

    private Transform parent;
    private ArrayList<Transform> children = new ArrayList<>();

    /**
     * Use only if you know what youre doing
     */
    public Transform()
    {
        position = new Vector2();
        rotation = 0;
        scale = new Vector2(1, 1);
        transform = this;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getLocalPosition()
    {
        if (parent == null) return position;

        Vector2 result = parent.position.subtract(position);
        return result;
    }

    public Vector2 getWorldPosition()
    {
        return position;
    }

    public void setPosition(Vector2 newPos)
    {
        Vector2 delta = newPos.subtract(position);
        position = newPos;

        for (Transform child : children)
        {
            child.position = child.position.add(delta);
        }
    }

    public float getRotationRadians()
    {
        return (float) Math.toRadians(rotation);
    }

    public float getWorldRotation()
    {
        return rotation;
    }

    public float getWorldRotationRadians()
    {
        return (float) Math.toRadians(rotation);
    }

    public float getRotation()
    {
        if (parent == null) return rotation;

        return parent.rotation - rotation;
    }

    public void setRotation(float newRotation) {
        if (newRotation == rotation) return;

        double deltaRotation = Math.toRadians(newRotation - rotation);
        this.rotation = newRotation;

        // Rotate children accordingly
        for (Transform child : children) {
            Vector2 relativePosition = child.position.subtract(position);
            Vector2 rotatedPosition = rotateVector(relativePosition, deltaRotation);
            child.position = position.add(rotatedPosition);
            child.rotation += (float)Math.toDegrees(deltaRotation);
        }
    }

    public void addRotation(float delta)
    {
        setRotation(rotation + delta);
    }

    private Vector2 rotateVector(Vector2 vector, double angleInRadians)
    {
        double cos = Math.cos(angleInRadians);
        double sin = Math.sin(angleInRadians);
        double x = vector.x * cos - vector.y * sin;
        double y = vector.x * sin + vector.y * cos;
        return new Vector2((float)x, (float)y);
    }

    // stolen from WinterRose.Monogame framework for C#
    public void lookAt(Vector2 targetPosition)
    {
        Vector2 direction = targetPosition.subtract(position).normalize();
        float angle = (float) Math.toDegrees(Math.atan2(direction.y, direction.x));
        setRotation(angle);
    }

    public Vector2 getScale()
    {
        return scale;
    }

    public Transform getParent()
    {
        return parent;
    }

    public void setParent(Transform newParent)
    {
        if (newParent == null && parent != null) parent.removeChild(this);
        if (newParent == this) throw new IllegalStateException("Cant assign this transform as its own parent");

        newParent.addChild(this);
        parent = newParent;
    }

    public void addChild(Transform child)
    {
        children.add(child);
    }

    public void removeChild(Transform child)
    {
        children.remove(child);
    }

    public void moveX(float newX)
    {
        setPosition(new Vector2(newX, position.y));
    }

    public void moveY(float newY)
    {
        setPosition(new Vector2(position.x, newY));
    }

    public void moveXY(float newX, float newY)
    {
        setPosition(new Vector2(newX, newY));
    }

    public void moveXY(Vector2 newPos)
    {
        setPosition(newPos);
    }

    public void translateX(float translation)
    {
        setPosition(new Vector2(position.x + translation, position.y));
    }

    public void translateY(float translation)
    {
       setPosition(new Vector2(position.x, position.y + translation));
    }

    public void translateXY(float translationX, float translationY)
    {
        setPosition(new Vector2(position.x + translationX, position.y + translationY));
    }

    public void translateXY(Vector2 delta)
    {
        translateXY(delta.x, delta.y);
    }

    public void setScale(Vector2 value)
    {
        scale = value;
    }
}
