package TPE.MagicCards;

import TPE.Magic;
import TPE.Monster;

public class DescargaElectrica extends Magic {
    public DescargaElectrica(String name, String description){
        super(name, description);
    }

    @Override
    public void effect(Monster m){
        m.setHealth(m.getHealth() - 50);
    }

}
