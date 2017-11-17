package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Castle implements Serializable{
    private int life;
    public static final int CASTLELIFE = 400;
    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva instancia de castillo, le asigna como vida la constante de vida CASTLELIFE.
     */
    public Castle() {
        this.life = CASTLELIFE;
    }


    /**
     * Se reduce de su vida el daño al ser atacado.
     * @param damage Daño que se inflinge al castillo.
     */
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
