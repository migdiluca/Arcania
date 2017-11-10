package back;

import back.Magic;
import back.Soldier;

public class DescargaElectrica extends Magic {
    public DescargaElectrica(String name, int id, String description){
        super(name, id, description);
    }

    public void effect(Soldier m){
        m.setHealth(m.getHealth() - 50);
    }

}
