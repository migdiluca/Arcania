package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Clase que modela las cartas. El campo graphicID cumple la función de permitir al cliente identificar los graficos
 */

public abstract class Card implements Serializable {
    private String name;
    private String description;
    private int graphicID;
    private Player owner;


    public Card(String name, int graphicID, String description) {
          this.name = name;
          this.graphicID = graphicID;
          this.description = description;
    }

    public String getName(){
        return name;
    }

    public int getID() {
        return graphicID;
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
        out.writeInt(graphicID);
        out.writeObject(owner);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        name = (String) ois.readObject();
        description = (String) ois.readObject();
        graphicID = ois.readInt();
        owner = (Player) ois.readObject();
    }

}
