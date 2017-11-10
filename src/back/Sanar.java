package back;

import back.Magic;
import back.Soldier;

public class Sanar extends Magic {
        public Sanar(String name, int id, String description){
            super(name, id, description);
        }

        public void effect(Soldier m){
            m.setHealth(m.getHealth() + 25);
        }

}

