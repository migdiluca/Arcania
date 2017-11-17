package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * Modela hechizo de tipo curación
 */

public class Heal extends Magic implements Serializable {
    private int points;

    public Heal(String name, int id, String description, boolean isNegative, int points, int duration){
        super(name, id, description, duration, isNegative);
        this.points = points;
    }

    /**
     * El conjuro cura y remueve todos los estados negativos
     * @param s soldado sobre el que se aplica
     */
    @Override
    public void startEffect(Soldier s){
        s.setHealth(s.getHealth() + points);
        Map<Magic, Integer> effectList = s.getAffectedBy();

        Iterator<Magic> i = effectList.keySet().iterator();

        while(i.hasNext()) {
            Magic m = i.next();
            if (m.getIsNegative()) {
                i.remove();
            }
        }
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(points);
    }

    public void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        points = ois.readInt();
    }
}
