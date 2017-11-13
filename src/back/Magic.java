package back;

public abstract class Magic extends Card {
    private String description;

    public Magic(String name, int id, String description){
        super(name, id, description);
        this.description = description;
    }

    public void effect(){}
}





