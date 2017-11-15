package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Inspiration extends Magic {
    private int buff;

    public Inspiration(String name, int id, String description, boolean isNegative){
        super(name, id, description, 5, isNegative);
        this.buff = buff;
    }

    @Override
    public void startEffect(Soldier s){
        s.setAttack(s.getAttack() + buff);
    }

    @Override
    public void lift(Soldier s) {
        s.setAttack(s.getAttack() - buff);
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(buff);
    }

    public void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        buff = ois.readInt();
    }
}
