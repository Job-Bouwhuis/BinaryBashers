package dev.WinterRose.SaxionEngine.TextProviders;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Stream<T>
{
    T[] items;
    int position;
    int endPosition;

    public Stream(T[] items)
    {
        this.items = items;
        position = -1;
        endPosition = items.length - 1;
    }

    public Stream(T[] items, int startPos)
    {
        this.items = items;
        position = startPos;
        endPosition = items.length - 1;
    }

    public Stream(T[] items, int startPos, int endPosition)
    {
        this.items = items;
        position = startPos;
        this.endPosition = endPosition;
    }

    public static Stream<Character> getCharacterStream(String s)
    {
        Character[] c = new Character[s.length()];
        for (int i = 0; i < s.length(); i++)
        {
            char ch = s.charAt(i);
            c[i] = ch;
        }
        return new Stream(c);
    }

    public T takeNext()
    {
        if(position >= endPosition)
            return null;

        T item = items[++position];
        return item;
    }

    public void moveNext()
    {
        position++;
    }

    public void dispose()
    {
        items = null;
        position = -1;
        endPosition = -1;
    }
}
