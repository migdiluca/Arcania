package back;

import java.awt.*;

public class Monster extends Card {
    private int attack;
    private int health;
    private int defense;
    private int agility;
    private int positionX;
    private int positionY;
    private Player owner;

    public Monster(String name, int attack, int health, int defense, int agility) {
        super(name);
        this.attack = attack;
        this.agility = agility;
        this.health = health;
        this.defense = defense;
    }


   // Posicion en la que se coloca la carta cuando se convoca.
   public void setPosition(Point p){
        this.positionX = p.x;
        this.positionY = p.y;
   }

   public Point getPosition(){
       return new Point(positionX, positionY);
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
    public void attack(Monster m){
        m.getAttacked(this.attack - m.getDefense());
    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    public Player getOwner() {
        return owner;
    }

    public int getOwnerNumber() {
        return 1;
    }

    private void getAttacked(int damage) {
        setHealth(this.health -= damage);
    }

}

