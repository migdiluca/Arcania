package back;

public abstract class Card {
    private String name;
    private Player owner;

    public Card( String name){
          this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
