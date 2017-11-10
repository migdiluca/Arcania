package back;

import java.awt.*;
import java.util.Random;

public class Soldier extends Card {
    private int attack;
    private int health;
    private int fullHealth;
    private int defense;
    private int agility;

    public Soldier(String name, int id, int attack, int health, int defense, int agility) {
        super(name, id);
        this.attack = attack;
        this.agility = agility;
        this.fullHealth = this.health = health;
        this.defense = defense;
    }

    public int getAgility() {
        return agility;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack){this.attack = attack;}

    public int getHealth() {
        return health;
    }

    public int getHealthPercent() {
        return (int) ((health*70) / (float) fullHealth);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getDefense() {
        return defense;
    }

    public void setHealth(int health){
       this.health = health;
    }

    // Estructura de ataque.
    public void attack(Soldier m){
            m.getAttacked(this.attack -  (this.attack * (m.getDefense() / 100)));
    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    private void getAttacked(int damage) {

        //int missChance = java.util.concurrent.ThreadLocalRandom.current().nextInt(agility, 100 + 1);

        Random r = new Random();
        int missChance = r.nextInt((100 - agility) + 1) + agility;

        System.out.println(getName());
        System.out.println(missChance);

        if(missChance < 85)
            setHealth(this.health - damage);
    }

}

