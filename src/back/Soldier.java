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
    public int attack(Soldier m){

        Random r = new Random();
        int missChance = r.nextInt((100 - m.getAgility()) + 1) + m.getAgility();

        if(missChance < 85) {
            m.getAttacked(this.attack -  (this.attack * (m.getDefense() / 100)), this);
            return 1;
        } else {
            return 0;
        }

    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    private void getAttacked(int damage, Soldier attacker) {


                 setHealth(this.health - damage);
    }

}

