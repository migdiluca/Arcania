package back.MagicCards;

import back.Magic;
import back.Monster;

public class Sanar extends Magic {
        public Sanar(String name, String description){
            super(name, description);
        }

        @Override
        public void effect(Monster m){
            m.setHealth(m.getHealth() + 25);
        }

}

