package back;

public abstract class Card {
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

}
