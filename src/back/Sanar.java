package TPE.MagicCards;

import TPE.Magic;
import TPE.Monster;

public class Sanar extends Magic {
        public Sanar(String name, String description){
            super(name, description);
        }

        @Override
        public void effect(Monster m){
            m.setHealth(m.getHealth() + 25);
        }

}

