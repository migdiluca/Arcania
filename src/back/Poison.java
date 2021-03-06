package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Clase que modela un hechizo de tipo veneno.
 * Los hechizos de tipo veneno infligen un faño por turno durante un número de turnos definido por la variable duration.
 */

public class Poison extends Magic implements Serializable{
    private int damage;

    public Poison(String name, int id, String description, boolean isNegative, int damage, int duration) {
        super(name, id, description, 5, isNegative);
        this.damage = damage;
    }

    @Override
    public void effect(Soldier s){
        s.setHealth(s.getHealth() - damage);
    }


    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(damage);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        damage =  ois.readInt();

    }
}
