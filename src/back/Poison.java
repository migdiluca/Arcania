package back;

public class Poison extends Magic {
    public Poison(String name, int id, String description){
        super("Veneno", 121, "Esta carta genera daño", 5);
    }

    public void effect(Soldier m){
        if (!m.isAlive()) {
            removeSoldier(m);
            return;
        }
        m.setHealth(m.getHealth() - 5);
    }
}
