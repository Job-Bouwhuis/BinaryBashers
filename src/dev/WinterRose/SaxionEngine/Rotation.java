package dev.WinterRose.SaxionEngine;

import java.util.function.Consumer;

/**
 * This class exists only for the infuriating limitation of no properties in java. that everything has to have a method that has to be called with ()
 */
public class Rotation
{
    private float rotation;

    public Rotation() { }

    public Rotation(float rotation)
    {
        this.rotation = rotation;
    }

    public Rotation add(float value)
    {
        rotation += value;
        return this;
    }

    public Rotation add(Rotation value)
    {
        rotation += value.rotation;
        return this;
    }

    public Rotation set(float value)
    {
        rotation = value;
        return this;
    }

    public Rotation set(Rotation value)
    {
        rotation = value.rotation;
        return this;
    }

    public Rotation subtract(float value)
    {
        rotation -= value;
        return this;
    }

    public Rotation subtract(Rotation value)
    {
        rotation -= value.rotation;
        return this;
    }

    public Rotation multiply(float value)
    {
        rotation *= value;
        return this;
    }

    public Rotation devide(float value)
    {
        if (value == 0 && rotation == 0) return this;
        rotation /= value;
        return this;
    }

    public float getDegrees()
    {
        return rotation;
    }

    public float getRadians()
    {
        return (float) Math.toRadians(rotation);
    }

    public Rotation clone()
    {
        var r = new Rotation();
        r.rotation = rotation;
        return r;
    }

    Rotation setInternal(Rotation rotation)
    {
        this.rotation = rotation.rotation;
        return this;
    }
    Rotation addInternal(Rotation value)
    {
        rotation += value.rotation;
        return this;
    }
}
