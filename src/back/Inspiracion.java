package TPE.MagicCards;

import TPE.Magic;
import TPE.Monster;

public class Inspiracion extends Magic {
    public Inspiracion(String name, String description){
        super(name, description);
    }

    @Override
    public void effect(Monster m){
        m.setAttack(m.getAttack() + 5);
    }
}
