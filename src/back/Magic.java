package back;

import java.util.HashSet;
import java.util.Set;

public abstract class Magic extends Card {
    private int duration;
    private Set<Soldier> affectedSoldiers = new HashSet<>();
    private String description;

    public Magic(String name, int id, String description, int duration){
        super(name, id, description);
        this.description = description;
        this.duration = duration;
    }

    public void applyEffect() {
        for (Soldier s: affectedSoldiers) {
            effect(s);
        }
        duration--;
    }

    public int getDuration () {
        return duration;
    }

    public void addSoldier(Soldier s) {
        affectedSoldiers.add(s);
    }

    public void removeSoldier(Soldier s) {
        affectedSoldiers.remove(s);
    }

    abstract public void effect(Soldier s);
}
