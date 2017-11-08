package back;

public class Castle {
    private int life;
    private static final int defaultLife = 1000;

    public Castle() {
        this.life = defaultLife;
    }
    public void getAttacked(int damage) {
        life -= damage;
    }

    public int getLife() {
        return life;
    }
}
