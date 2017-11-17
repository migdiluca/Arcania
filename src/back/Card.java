package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Card implements Serializable {
    private String name;
    private String description;
    private int id;
    private Player owner;


    public Card(String name, int id, String description) {
          this.name = name;
          this.id = id;
          this.description = description;
    }

    public String getName(){
        return name;
    }

    public int getID() {
        return id;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getDescription() { return description; }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
        out.writeObject(description);
        out.writeInt(id);
        out.writeObject(owner);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        name = (String) ois.readObject();
        description = (String) ois.readObject();
        id = ois.readInt();
        owner = (Player) ois.readObject();
    }

}
