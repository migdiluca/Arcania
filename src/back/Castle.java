package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Castle implements Serializable{
    private int life;
    public static final int CASTLELIFE = 1000;
    private static final long serialVersionUID = 1L;

    public Castle() {
        this.life = CASTLELIFE;
    }

    public void getAttacked(int damage) {
        life -= damage;
    }

    public int getLife() {
        return life;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(life);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        life = (int) ois.readObject();
    }
}
