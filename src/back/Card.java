package back;

public abstract class Card {
    private String name;
    private Player owner;
    private int id;

    public Card(String name, int id) {
          this.name = name;
          this.id = id;
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
}
