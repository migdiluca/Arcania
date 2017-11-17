package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Clase que modela los hechizos que producen un daño instantáneo
 */

public class MagicDamage extends Magic implements Serializable {
    private int damage;
    public MagicDamage(String name, int id, String description, boolean isNegative, int damage){
        super(name, id, description, 0, isNegative);
        this.damage = damage;
    }

    @Override
    public void startEffect(Soldier m){
        m.setHealth(m.getHealth() - damage);
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(damage);
    }

    public void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        damage = ois.readInt();
    }
}
