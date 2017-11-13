package back;

public class MagicDamage extends Magic {
    public MagicDamage(String name, int id, String description, boolean isNegative){
        super(name, id, description, 0, isNegative);
    }

    @Override
    public void startEffect(Soldier m){
        m.setHealth(m.getHealth() - 50);
    }
}
