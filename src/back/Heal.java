package back;

public class Heal extends Magic {
    public Heal(String name, int id, String description){
        super("Sanar", 120, "Esta carta cura vida.", 1);
    }

    public void effect(Soldier s){
        if (!s.isAlive()) {
            removeSoldier(s);
            return;
        }
        s.setHealth(s.getHealth() + 25);
    }
}
