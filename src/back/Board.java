package back;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Board {
    private Soldier board[][];

    public Board() {
        board = new Soldier[7][7];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                board[i][j] = null;
    }

    private boolean isPointValid(int x, int y) {
        if (x >= 0 && x < 7 && y >= 0 && y < 7)
            return true;
        return false;
    }

    private Soldier getSoldier(Point p) {
        return board[p.x][p.y];
    }

    private boolean areEnemies(Soldier s1, Soldier s2) {
        if(s1 == null || s2 == null)
            return false;
        return s1.getOwner() != s2.getOwner();
    }

    /* Devuelve todos los puntos(diagonal incluido) pegados al que se encuentra en el momento, comprobando
    que sean validos (no se vayan de la matriz) */
    private ArrayList<Point> nearbyPoints(Point p) {
        ArrayList<Point> nearbyPoints = new ArrayList<Point>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isPointValid(p.x +i, p.y+j) && (j != 0 || i != 0))
                    nearbyPoints.add(new Point(p.x+i, p.y+j));
            }
        }
        return nearbyPoints;
    }

    /*PRIVATE*/
    /* Se fija en todos los puntos pegados, si no hay un enemigo o si son diagonales los quita */
    private ArrayList<Point> validAttackPoints(Point p, Soldier s) {
        ArrayList<Point> validAttackPoints = nearbyPoints(p);
        Iterator<Point> iterator = validAttackPoints.iterator();
        while(iterator.hasNext()) {
            Point z = iterator.next();
            if((z.x != p.x && z.y != p.y) || !areEnemies(s, getSoldier(z))) {
                iterator.remove();
            }
        }
        return validAttackPoints;
    }

    public void addSoldier(Soldier s, Point p) {
        board[p.x][p.y] = s;
    }

    /* Se fija en todos los puntos pegados (incluido diagonales), lo quita si:
        - Hay un monstruo
        - Se fija en los puntos si son diagonales y es un heroe. Si NO cumple esto lo quita.
    Luego agrega el punto de retirada.
     */
    public HashMap<Point, Boolean> validMovePoints(Point p) {
        if(getSoldier(p) == null)
            return new HashMap<Point,Boolean>();
        HashMap<Point,Boolean> validMoveMapPoints = new HashMap<>();
        ArrayList<Point> validMovePoints = nearbyPoints(p);
        Iterator<Point> iterator = validMovePoints.iterator();
        while(iterator.hasNext()) {
            Point z = iterator.next();
            if ((z.x != p.x && z.y != p.y && !(getSoldier(p) instanceof Heroe)) || getSoldier(z) != null) {
                iterator.remove();
                System.out.println("remove diagonal:" + z);
            }
        }

        int closerToBase = getSoldier(p).getOwner().getCastleRow() == 0 ? -1 : 1;
        Point surrenderPoint = new Point(p.x + closerToBase, p.y);

        if(validMovePoints.contains(surrenderPoint) && !validAttackPoints(p, getSoldier(p)).isEmpty()){
            surrenderPoint.translate(closerToBase, 0);
            if(isPointValid(surrenderPoint.x , surrenderPoint.y) && board[surrenderPoint.x][surrenderPoint.y] == null)
                validMovePoints.add(surrenderPoint);
        }
        for(Point z: validMovePoints) {
            if(!validAttackPoints(z, getSoldier(p)).isEmpty())
                validMoveMapPoints.put(z, true);
            else
                validMoveMapPoints.put(z, false);
        }

        return validMoveMapPoints;
    }

    public boolean canAttackCastle(Point p, int playerNumber){
        int attackRow = playerNumber == 0 ? 6 : 0;
        if(p.x == attackRow)
            return true;
        return false;
    }

    /*ESTO NO SE SI LO TIENE QUE HACER GAME O BOARD, me daria lo mismo pero hay que ponerlo donde tiene que ir.
    Se fija en todos los puntos validos de ataque cual es el de menor vida
     */
    public Soldier enemyToAttack (Point p) {
        ArrayList<Point> validAttackPoints = validAttackPoints(p, getSoldier(p));
        Soldier SoldierToAttack = null;
        for (Point x : validAttackPoints) {
            if (areEnemies(getSoldier(p), getSoldier(x)))
                if(SoldierToAttack == null || SoldierToAttack.getHealth() > getSoldier(x).getHealth())
                    SoldierToAttack = getSoldier(x);
        }
        return SoldierToAttack;
    }

    public void moveSoldier(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    public void removeDeadFromBoard(Soldier s) {
        Point p = searchSoldier(s);
        board[p.x][p.y] = null;
    }

    public Point searchSoldier(Soldier s) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if(board[i][j] == s)
                    return new Point(i,j);
            }
        }
        return null; //Meter una excepcion
    }

    public ArrayList<Point> availableSpawns(int playerNumber) {
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        int spawnRow = playerNumber == 0 ? 0 : 6;
        for (int j = 0; j < 7; j++)
            if (board[spawnRow][j] == null)
                availablePoints.add(new Point(spawnRow, j));
        return availablePoints;
    }
}