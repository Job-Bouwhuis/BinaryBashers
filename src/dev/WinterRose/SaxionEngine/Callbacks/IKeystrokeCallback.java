package dev.WinterRose.SaxionEngine.Callbacks;

import java.awt.event.KeyEvent;

public interface IKeystrokeCallback extends ICallback
{
    void keyPress(KeyEvent key);
}
