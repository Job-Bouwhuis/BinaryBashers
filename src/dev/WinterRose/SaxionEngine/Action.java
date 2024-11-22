package dev.WinterRose.SaxionEngine;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Action<T>
{
    private ArrayList<Consumer<T>> consumers;

    public void invoke(T arg)
    {
        for(var consumer : consumers)
            consumer.accept(arg);
    }

    public void add(Consumer<T> consumer)
    {
        consumers.add(consumer);
    }

    public void remove(Consumer<T> consumer)
    {
        consumers.remove(consumer);
    }
}
