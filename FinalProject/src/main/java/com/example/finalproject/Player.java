package com.example.finalproject;

import java.util.Random;

public class Player {
    private int health = 20;
    private int strength;
    private int dexterity;
    private int intelligence;
    private int gold = 0;
    public Player() {
        this.strength = generatePlayerStats();
        this.dexterity = generatePlayerStats();
        this.intelligence = generatePlayerStats();

    }
    public Integer generatePlayerStats(){
        Random random = new Random();
        int diceRoll = 0;
        for (int i = 0; i < 3; i++){
            diceRoll += random.nextInt(1,7);
        }
        return diceRoll;
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
    public int getGold() {
        return gold;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setGold(int gold) {
        this.gold = gold;
    }
    public boolean doIntelligenceCheck(){
        Random random = new Random();
        int diceRoll = random.nextInt(1,21);
        return diceRoll < intelligence;
    }
}
