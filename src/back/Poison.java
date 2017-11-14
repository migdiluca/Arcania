package back;

public class Poison extends Magic {
    private int damage;

    public Poison(String name, int id, String description, boolean isNegative, int damage, int duration) {
        super(name, id, description, 5, isNegative);
        this.damage = damage;
    }

    @Override
    public void effect(Soldier s){
        s.setHealth(s.getHealth() - damage);
    }
}
