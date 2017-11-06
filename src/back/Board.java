package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Board {
    private Monster board[][];

    public Board() {
        board = new Monster[7][7];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                board[i][j] = null;
    }

    private boolean isPointValid(int x, int y) {
        if (x >= 0 && x < 7 && y >= 0 && y < 7)
            return true;
        return false;
    }

    /*PRIVATE*/
    public Monster getMonster(Point p) {
        return board[p.x][p.y];
    }

    private boolean areEnemies(Monster m1, Monster m2) {
        if(m2 == null)
            return false;
        if(m1.getOwner() != m2.getOwner())
            return true;
        return false;
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
    public ArrayList<Point> validAttackPoints(Point p) {
        ArrayList<Point> validAttackPoints = nearbyPoints(p);
        Iterator<Point> iterator = validAttackPoints.iterator();
        while(iterator.hasNext()) {
            Point z = iterator.next();
            if((z.x != p.x && z.y != p.y) || !areEnemies(getMonster(p), getMonster(z)))
                iterator.remove();
        }
        return validAttackPoints;
    }

    public void addMonster(Monster m, Point p) {
        board[p.x][p.y] = m;
    }

    /* Se fija en todos los puntos pegados (incluido diagonales), lo quita si:
        - Hay un monstruo
        - Se fija en los puntos si son diagonales y es un heroe. Si NO cumple esto lo quita.
    Luego agrega el punto de retirada.
     */
    public ArrayList<Point> validMovePoints(Point p) {
        ArrayList<Point> validMovePoints = nearbyPoints(p);
        Iterator<Point> iterator = validMovePoints.iterator();
        while(iterator.hasNext()) {
            Point z = iterator.next();
            if (!((z.x != p.x && z.y != p.x && getMonster(p) instanceof Heroe) || getMonster(z) == null))
                iterator.remove();
        }

        int closerToBase = getMonster(p).getOwnerNumber() == 0 ? -1 : 1;
        Point surrenderPoint = new Point(p.x + closerToBase, p.y);

        if(validMovePoints.contains(surrenderPoint) && validAttackPoints(p) != null){
            surrenderPoint.translate(closerToBase, 0);
            validMovePoints.add(surrenderPoint);
        }
        return validMovePoints;
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
    public Monster enemyToAttack (Point p) {
        ArrayList<Point> validAttackPoints = validAttackPoints(p);
        Monster monsterToAttack = null;
        for (Point x : validAttackPoints) {
            if (areEnemies(getMonster(p), getMonster(x)))
                if(monsterToAttack == null || monsterToAttack.getHealth() > getMonster(x).getHealth())
                    monsterToAttack = getMonster(x);
        }
        return monsterToAttack;
    }

    public void moveMonster(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    public void removeDeadFromBoard(Monster m) {
        Point p = searchMonster(m);
        board[p.x][p.y] = null;
    }

    public Point searchMonster(Monster m) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if(board[i][j] == m)
                    return new Point(i,j);
            }
        }
        return null; //esto es medio turbio, lo hice porque me pide un return, pero al mounstro siempre lo encuentra
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