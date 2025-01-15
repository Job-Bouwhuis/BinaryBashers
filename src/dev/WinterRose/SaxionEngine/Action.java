package dev.WinterRose.SaxionEngine;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Action<T>
{
    private ArrayList<Consumer<T>> consumers = new ArrayList<>();

    public void invoke(T arg)
    {
        for (var consumer : consumers)
        {
            try
            {
                consumer.accept(arg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void add(Consumer<T> consumer)
    {
        if(consumer == null)
            return;
        consumers.add(consumer);
    }

    public void remove(Consumer<T> consumer)
    {
        if(consumer == null)
            return;
        consumers.remove(consumer);
    }

    public void clear()
    {
        consumers.clear();
    }

    public boolean any()
    {
        return !consumers.isEmpty();
    }
}
