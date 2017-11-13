package back;

public class Inspiration extends Magic {
    public Inspiration(String name, int id, String description){
        super("Inspiración", 123, "Aumenta el ataque durante 5 turnos", 5);
    }

    public void effect(Soldier s){
        if (!s.isAlive()) {
            removeSoldier(s);
            return;
        }
        if (getDuration() == 5) {
            s.setAttack(s.getAttack() + 5);
        } else if (getDuration() == 1) {
            s.setAttack(s.getAttack() - 5);
        }
    }
}
