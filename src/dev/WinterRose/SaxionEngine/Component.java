package dev.WinterRose.SaxionEngine;

public abstract class Component
{
    public GameObject owner;
    public Transform transform;

    public void awake() {}
    public void onDestroyed() {}
}
