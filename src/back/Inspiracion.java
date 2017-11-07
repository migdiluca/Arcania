package back;

import back.Magic;
import back.Monster;

public class Inspiracion extends Magic {
    public Inspiracion(String name, String description){
        super(name, description);
    }

    public void effect(Soldier m){
        m.setAttack(m.getAttack() + 5);
    }
}
