package com.example.finalproject;

import java.util.Random;

public class NonPlayerCharacter {
    private int health;
    private int strength;
    private int dexterity;
    private int intelligence;

    public NonPlayerCharacter() {
        Random random = new Random();
        this.health = random.nextInt(1,7);;
        this.strength = health * 2;
        this.dexterity = health * 2;
        this.intelligence = health * 2;
    }
    public NonPlayerCharacter(int health, int strength, int dexterity, int intelligence) {
        this.health = health;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }
    public int getHealth() {
        return health;
    }
    public int getStrength() {
        return strength;
    }
    public int getDexterity() {
        return dexterity;
    }
    public int getIntelligence() {
        return intelligence;
    }
}
