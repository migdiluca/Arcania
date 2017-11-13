package back;

import java.awt.*;

public class Hero extends Soldier {
    public Hero(String name, int id, int attack, int health, int defense, int agility, String description){
        super(name,id,attack,health,defense,agility, description);
    }

    public void throwMagic(Magic m){
        m.effect();
    }

}
