import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.lang.annotation.Retention;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {

    back.Soldier s1 = null;
    back.Soldier s2 = null;
    back.Soldier s3 = new back.Soldier("fer",1,10,10,10,10,"genio");
    back.Board b1 = new back.Board();
    Point p= new Point(1,2);
    Point r = new Point(2,2);
    back.Poison poison = new back.Poison("veneno",1,"saca vida",true,5,5);
    back.Inspiration inspiration = new back.Inspiration("inspiracion",1,"agrega ataque",false);

    @Test
    public void addSoldierTest(){
        b1.addSoldier(s1,p);
        assertTrue(s1 == b1.getSoldier(p));
    }

    @Test
    public void moveSoldierTest(){
        b1.moveSoldier(p,r);
        assertTrue(b1.getSoldier(p) == null);
    }

    @Test
    public void removeDeadFromBoardTest(){
        b1.addSoldier(s3,p);
        b1.removeDeadFromBoard(s3);
        assertTrue(b1.getSoldier(p) == null);

    }

    @Test
    public void searchSoldierTest(){
        Point f = b1.searchSoldier(s3);
        assertTrue(f == null);
    }

    @Test
    public void poisonTest(){
        poison.effect(s3);
        assertTrue(s3.getHealth() == 5);
    }

    @Test
    public void inspirationTest(){
        inspiration.startEffect(s3);
        assertTrue(s3.getAttack() == 15);
        inspiration.lift(s3);
        assertTrue(s3.getAttack() == 10);
    }


}