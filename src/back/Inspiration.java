package back;

public class Inspiration extends Magic {
    private int buff;

    public Inspiration(String name, int id, String description, boolean isNegative){
        super(name, id, description, 5, isNegative);
        this.buff = buff;
    }

    @Override
    public void startEffect(Soldier s){
        s.setAttack(s.getAttack() + buff);
    }

    @Override
    public void lift(Soldier s) {
        s.setAttack(s.getAttack() - buff);
    }
}
