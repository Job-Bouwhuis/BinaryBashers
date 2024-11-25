package dev.WinterRose.SaxionEngine;

public class Vector2
{
    public float x;
    public float y;

    public Vector2()
    {
        this(0);
    }

    public Vector2(float xy)
    {
        x = y = xy;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 original)
    {
        x = original.x;
        y = original.y;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vector2 other) if (x == other.x) return y == other.y;
        return false;
    }

    /**
     * @return The length of this Vector2
     */
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Gets the distance between this vector, and the other
     * @return 0
     */
    public float distance(Vector2 other)
    {
        float deltaX = other.x - x;
        float deltaY = other.y - y;

        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    /**
     * Creates a new vector with the normalized value of this one.
     * @return A new vector2 with normalized values of the original
     */
    public Vector2 normalized()
    {
        return new Vector2(this).normalize();
    }

    /**
     * Normalizes this vector. But only if x and y are not 0.
     * @return returns this same instance
     */
    public Vector2 normalize()
    {
        if (x == 0 && y == 0) return this;
        float length = length();
        x = x / length;
        y = y / length;
        return this;
    }

    public Vector2 clone()
    {
        return new Vector2(this);
    }

    public Vector2 add(Vector2 other)
    {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2 multiply(float value)
    {
        x *= value;
        y *= value;
        return this;
    }

    public Vector2 multiply(Vector2 value)
    {
        x *= value.x;
        y *= value.y;
        return this;
    }

    public Vector2 subtract(Vector2 vector2)
    {
        Vector2 inverted = vector2.invert();
        add(inverted);
        return this;
    }

    private Vector2 invert()
    {
        return new Vector2(-x, -y);
    }

    public void set(Vector2 value)
    {
        x = value.x;
        y = value.y;
    }

    @Override
    public String toString()
    {
        return "X: %s - Y: %s".formatted(x, y);
    }
}
