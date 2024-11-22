package dev.WinterRose.SaxionEngine.Callbacks;

import nl.saxion.app.interaction.KeyboardEvent;

public interface IKeystrokeCallback extends ICallback
{
    void keyPress(KeyboardEvent key);
}
