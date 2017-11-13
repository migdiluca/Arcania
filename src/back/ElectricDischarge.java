package back;

public class ElectricDischarge extends Magic {
    public ElectricDischarge (String name, int id, String description){
        super("Descarga eléctrica", 122, "Descarga eléctrica que genera daño instantáneo", 1);
    }

    public void effect(Soldier m){
        if (!m.isAlive()) {
            removeSoldier(m);
            return;
        }
        m.setHealth(m.getHealth() - 50);
    }
}
