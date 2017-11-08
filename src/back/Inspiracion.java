package back;

public class Inspiracion extends Magic {
    public Inspiracion(String name, String description){
        super(name, description);
    }

    public void effect(Soldier s){
        s.setAttack(s.getAttack() + 5);
    }
}
