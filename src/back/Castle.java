package back;

public class Castle {
    private int life;

    public Castle(int life) {
        this.life = life;
    }
    public void getAttacked(int damage) {
        life -= damage;
    }

    public int getLife() {
        return life;
    }
}
