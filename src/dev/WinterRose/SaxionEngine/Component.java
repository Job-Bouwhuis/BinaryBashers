package dev.WinterRose.SaxionEngine;

import dev.WinterRose.SaxionEngine.ColorPallets.ColorPallet;

public abstract class Component
{
    public GameObject owner;
    public Transform transform;

    public void awake() {}
    public void onDestroyed() {}

    public void onColorPalleteChange(ColorPallet colorPallet) {};
}
