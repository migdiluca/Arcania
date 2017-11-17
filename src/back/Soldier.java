package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Soldier extends Card implements Serializable{
    private int attack;
    private int health;
    private int fullHealth;
    private int defense;
    private int agility;
    private boolean alreadyMoved;

    private static final long serialVersionUID = 1L;
    private static Random r = new Random();
    private HashMap<Magic, Integer> affectedBy = new HashMap<>();

    /**
     * Crea una nueva instancia de soldado.
     * @param name Nombre del soldado.
     * @param id ID del soldado.
     * @param attack Puntos de ataque.
     * @param health Vida.
     * @param defense Puntos de defensa.
     * @param agility Puntos de agilidad.
     * @param description Descripcion del soldado.
     */
    public Soldier(String name, int id, int attack, int health, int defense, int agility, String description) {
        super(name, id, description);
        this.attack = attack;
        this.agility = agility;
        this.fullHealth = this.health = health;
        this.defense = defense;
        this.alreadyMoved = false;
    }

    /**
     * Aplica el estado inicial del hechizo y lo agrega a la lista de estados, si corresponde
     * @param m hechizo que se aplica sobre el soldado
     */
    public void curse(Magic m) {
        m.startEffect(this);
        if (m.getDuration() > 0) {
            affectedBy.put(m, m.getDuration());
        }
    }

    public HashMap<Magic, Integer> getAffectedBy () {
        return affectedBy;
    }

    /**
     * Aplica los efectos por turno de los estados que modifican al soldado.
     */
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

    /**
     * Realiza el ataque a un soldado que se le envia como parametro.
     * @param s Soldado a atacar.
     * @return Retorna 1 si lo puedo atacar, si no retorna 0.
     */
    public int attack(Soldier s){

        int missChance = r.nextInt((100 - s.getAgility()) + 1) + s.getAgility();

        if(missChance < 85) {
            s.getAttacked(this.attack -  (this.attack * (s.getDefense() / 100)));
            return 1;
        } else {
            return 0;
        }
    }

    public void attackCastle(Castle c){
        c.getAttacked(this.attack);
    }

    /**
     * Reduce su vida de acuerdo al daño recibido.
     * @param damage Daño recibido.
     */
    private void getAttacked(int damage) {
        setHealth(this.health - damage);
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        super.writeObject(out);
        out.defaultWriteObject();
        out.writeInt(attack);
        out.writeInt(health);
        out.writeInt(defense);
        out.writeInt(agility);
        out.writeObject(affectedBy);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        super.readObject(ois);
        ois.defaultReadObject();
        attack =  ois.readInt();
        health =  ois.readInt();
        defense = ois.readInt();
        agility = ois.readInt();
        affectedBy = (HashMap<Magic,Integer>) ois.readObject();

    }
}

