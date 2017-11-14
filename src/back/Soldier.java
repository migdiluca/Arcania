package back;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Soldier extends Card implements Serializable{
    private static final long serialVersionUID = 1L;
    private int attack;
    private int health;
    private int fullHealth;
    private int defense;
    private int agility;
    public boolean alreadyMoved;
    private Map<Magic, Integer> affectedBy = new HashMap<>();

    public void curse(Magic m) {
        m.startEffect(this);
        if (m.getDuration() > 0) {
            affectedBy.put(m, m.getDuration());
        }
    }

    public HashSet<String> GetAffectedBy() {
        HashSet<String> GetAffectedBy = new HashSet<>();
        for(Magic m: affectedBy.keySet())
            GetAffectedBy.add(m.getClass().toString());

        return GetAffectedBy;
    }

    public Map<Magic, Integer> getAffectedBy () {
        return affectedBy;
    }

    public void applyMagic() {
        for (Magic m: affectedBy.keySet()) {
            m.effect(this);

            int turnsLeft = affectedBy.get(m);
            if (affectedBy.get(m) == 0) {
                m.lift(this);
                affectedBy.remove(m);
            } else {
                affectedBy.replace(m, turnsLeft - 1);
            }
        }
    }

    public Soldier(String name, int id, int attack, int health, int defense, int agility, String description) {
        super(name, id, description);
        this.attack = attack;
        this.agility = agility;
        this.fullHealth = this.health = health;
        this.defense = defense;
        this.alreadyMoved = false;
    }

    public void enableMovement() {
        alreadyMoved = false;
    }

    public void disableMovement() {
        alreadyMoved = true;
    }

    public boolean canMove() {
        return !alreadyMoved;
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
       if (this.health > fullHealth)
           this.health = fullHealth;
    }

    // Estructura de ataque.
    public int attack(Soldier m){

        Random r = new Random();
        int missChance = r.nextInt((100 - m.getAgility()) + 1) + m.getAgility();

        if(missChance < 85) {
            m.getAttacked(this.attack -  (this.attack * (m.getDefense() / 100)));
            return 1;
        } else {
            return 0;
        }

    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    private void getAttacked(int damage) {
        setHealth(this.health - damage);
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(attack);
        out.writeInt(health);
        out.writeInt(defense);
        out.writeInt(agility);
        out.writeObject(affectedBy);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        attack =  ois.readInt();
        health =  ois.readInt();
        defense = ois.readInt();
        agility = ois.readInt();
        affectedBy = (Map<Magic,Integer>) ois.readObject();

    }
}

