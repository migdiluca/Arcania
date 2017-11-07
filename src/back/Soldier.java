package back;

import java.awt.*;

public class Soldier extends Card {
    private int attack;
    private int health;
    private int defense;
    private int agility;
    private Player owner;

    public Soldier(String name, Player owner, int attack, int health, int defense, int agility) {
        super(name);
        this.owner = owner;
        this.attack = attack;
        this.agility = agility;
        this.health = health;
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

    public Player getOwner() {
        return owner;
    }

    private void getAttacked(int damage) {
        setHealth(this.health -= damage);
    }

}

