package back;

import java.util.Iterator;
import java.util.Map;

public class Heal extends Magic {
    private int points;

    public Heal(String name, int id, String description, boolean isNegative, int points, int duration){
        super(name, id, description, duration, isNegative);
        this.points = points;
    }

    @Override
    public void startEffect(Soldier s){
        s.setHealth(s.getHealth() + points);
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
