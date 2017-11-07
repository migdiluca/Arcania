package back;

import back.Magic;
import back.Monster;

public class DescargaElectrica extends Magic {
    public DescargaElectrica(String name, String description){
        super(name, description);
    }

    public void effect(Soldier m){
        m.setHealth(m.getHealth() - 50);
    }

}
