package dev.WinterRose.SaxionEngine;

import java.util.ArrayList;

public class Queue<T>
{
    private ArrayList<T> items = new ArrayList<>();

    public void enqueue(T item)
    {
        items.add(item);
    }

    public T peek()
    {
        if(!items.isEmpty())
            return items.getFirst();

        return null;
    }

    public T pop()
    {
        if(items.isEmpty())
            return null;

        T item = items.getFirst();
        items.removeFirst();
        return item;
    }

    public int size()
    {
        return items.size();
    }
}
