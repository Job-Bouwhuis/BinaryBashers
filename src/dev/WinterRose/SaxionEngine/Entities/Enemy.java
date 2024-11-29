package dev.WinterRose.SaxionEngine.Entities;

public class Enemy extends Entity {
    int entityID;
    
    @Override public void death() {
        System.out.println("Enemy has been defeated.");
    }
}
