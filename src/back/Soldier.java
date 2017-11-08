package back;

import java.awt.*;

public class Soldier extends Card {
    private int attack;
    private int health;
    private int fullHealth;
    private int defense;
    private int agility;

    public Soldier(String name, int attack, int health, int defense, int agility) {
        super(name);
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
        return (int) ((health*100) / (float) fullHealth);
    }

    public boolean isAlive() {
        return health > 0 ? true : false;
    }

    public int getDefense() {
        return defense;
    }

    public void setHealth(int helath){
       this.health = health;
    }

    // Estructura de ataque.
    public void attack(Soldier m){
        m.getAttacked(this.attack - m.getDefense());
    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    private void getAttacked(int damage) {
        setHealth(this.health -= damage);
    }

}

