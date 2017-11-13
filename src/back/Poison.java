package back;

public class Poison extends Magic {
    public Poison(String name, int id, String description, boolean isNegative){
        super(name, id, description, 5, isNegative);
    }

    @Override
    public void effect(Soldier s){
        s.setHealth(s.getHealth() - 5);
    }
}
