package back;

public abstract class Magic extends Card {
    private int duration;
    private boolean isNegative;

    public Magic(String name, int id, String description, int duration, boolean isNegative){
        super(name, id, description);
        this.duration = duration;
        this.isNegative = isNegative;
    }

    public void effect(){}
}
    public boolean getIsNegative() {
        return isNegative;
    }

    public int getDuration() {
        return duration;
    }

    public void startEffect(Soldier s) {
        return;
    }

    public void effect(Soldier s){
        return;
    }

    public void lift(Soldier s) {
        return;
    }
}
