package back;

public class Inspiracion extends Magic {
    public Inspiracion(String name, int id, String description){
        super(name, id, description);
    }

    public void effect(Soldier s){
        s.setAttack(s.getAttack() + 5);
    }
}
