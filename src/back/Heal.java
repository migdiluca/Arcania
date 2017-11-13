package back;

import java.util.Iterator;
import java.util.Map;

public class Heal extends Magic {
    public Heal(String name, int id, String description, boolean isNegative){
        super(name, id, description, 0, isNegative);
    }

    @Override
    public void startEffect(Soldier s){
        s.setHealth(s.getHealth() + 25);
        Map<Magic, Integer> effectList = s.getAffectedBy();

        Iterator<Magic> i = effectList.keySet().iterator();

        while(i.hasNext()) {
            Magic m = i.next();
            if (m.getIsNegative()) {
                i.remove();
            }
        }
    }
}
