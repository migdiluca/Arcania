package back;

public abstract class Magic extends Card {
    private String description;

    public Magic(String name, String description){
        super(name);
        this.description = description;
    }

    public void effect(Monster m){};
}





