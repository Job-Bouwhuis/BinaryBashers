package dev.WinterRose.SaxionEngine;


import java.util.ArrayList;

public class Transform extends Component
{
    private Vector2 position; // the position of the object. may be relative to the parent
    private Rotation rotation;
    private Vector2 scale;

    private Transform parent;
    private ArrayList<Transform> children = new ArrayList<>();

    Transform()
    {
        position = new Vector2();
        rotation = new Rotation();
        scale = new Vector2(1, 1);
    }

    public Vector2 getPosition()
    {
        return calculateRelativePositionToParent();
    }

    public Rotation getRotation()
    {
        if (parent == null) return rotation;

        // TODO: fix back into not cursed form. its now cursed so it doesnt call .subtract()
        return new Rotation(parent.getRotation().getDegrees() - rotation.getDegrees());
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
        if (parent != null) parent.removeChild(this);
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

    public void moveX(int newX)
    {
        position = new Vector2(newX, position.y);
    }

    public void moveY(int newY)
    {
        position = new Vector2(position.x, newY);
    }

    public void moveXY(int newX, int newY)
    {
        position = new Vector2(newX, newY);
    }

    public void translateX(int translation)
    {
        position = new Vector2(position.x + translation, position.y);
    }

    public void translateY(int translation)
    {
        position = new Vector2(position.x, position.y + translation);
    }

    public void translationXY(int translationX, int translationY)
    {
        position = new Vector2(position.x + translationX, position.y + translationY);
    }

    private Vector2 calculateRelativePositionToParent()
    {
        if (parent == null) return position;

        Vector2 result = parent.position.clone().subtract(position);
        return result;
    }
}
