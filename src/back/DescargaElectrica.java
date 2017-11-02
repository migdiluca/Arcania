package back.MagicCards;

import back.Magic;
import back.Monster;

public class DescargaElectrica extends Magic {
    public DescargaElectrica(String name, String description){
        super(name, description);
    }

    @Override
    public void effect(Monster m){
        m.setHealth(m.getHealth() - 50);
    }

}
