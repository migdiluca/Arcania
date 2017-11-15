package back;

import java.io.Serializable;

public class Hero extends Soldier implements Serializable {
    public Hero(String name, int id, int attack, int health, int defense, int agility, String description){
        super(name,id,attack,health,defense,agility, description);
    }

    public void throwMagic(Magic m){
        m.effect();
    }

    @Override
    public void curse (Magic m) {}

}
