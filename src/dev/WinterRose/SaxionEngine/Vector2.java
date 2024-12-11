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
     * @return The distance between the two vectors
     */
    public float distance(Vector2 other)
    {
        float deltaX = other.x - x;
        float deltaY = other.y - y;

        return (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
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

//    public Vector2 clone()
//    {
//        return new Vector2(this);
//    }

    public Vector2 add(Vector2 other)
    {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 multiply(float value)
    {
        return new Vector2(x * value, y * value);
    }

    public Vector2 multiply(Vector2 value)
    {
        return new Vector2(x * value.x, y * value.y);
    }

    public Vector2 subtract(Vector2 value)
    {
        return new Vector2(x - value.x, y - value.y);
    }

    public Vector2 invert()
    {
        return new Vector2(-x, -y);
    }

    @Override
    public String toString()
    {
        return "X: %s - Y: %s".formatted(x, y);
    }

    public Vector2 divide(float divider)
    {
        return new Vector2(x / divider, y / divider);
    }
    public Vector2 divide(Vector2 divider)
    {
        return new Vector2(x / divider.x, y / divider.y);
    }

    public Vector2 abs()
    {
        return new Vector2(Math.abs(x), Math.abs(y));
    }

    public Vector2 lerp(Vector2 destination, float time) {
        float newX = this.x + (destination.x - this.x) * time;
        float newY = this.y + (destination.y - this.y) * time;
        return new Vector2(newX, newY);
    }
}
