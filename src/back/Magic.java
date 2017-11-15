package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Magic extends Card implements Serializable {
    private int duration;
    private boolean isNegative;

    public Magic(String name, int id, String description, int duration, boolean isNegative){
        super(name, id, description);
        this.duration = duration;
        this.isNegative = isNegative;
    }

    public void effect(){}

    public boolean getIsNegative() {
        return isNegative;
    }

    public int getDuration() {
        return duration;
    }

    public void startEffect(Soldier s) {
        return;
    }

    public void effect(Soldier s){
        return;
    }

    public void lift(Soldier s) {
        return;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(duration);
        out.writeBoolean(isNegative);
    }

    public void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        duration = ois.readInt();
        isNegative = ois.readBoolean();
    }
}
