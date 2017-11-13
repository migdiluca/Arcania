package back;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Board {
    private Soldier board[][];

    private Game game;
    private static final long serialVersionUID = 1L;

    public Board(Game game) {
        board = new Soldier[7][7];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                board[i][j] = null;

        this.game = game;
    }

    private boolean isPointValid(int x, int y) {
        return (x >= 0 && x < 7 && y >= 0 && y < 7);
    }

    private boolean areEnemies(Soldier s1, Soldier s2) {
        if (s1 == null || s2 == null)
            return false;
        return s1.getOwner() != s2.getOwner();
    }

    /* Devuelve todos los puntos(diagonal incluido) pegados al que se encuentra en el momento, comprobando
    que sean validos (no se vayan de la matriz) */

    private ArrayList<Point> nearbyPoints(Point p) {
        ArrayList<Point> nearbyPoints = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isPointValid(p.x + i, p.y + j) && (j != 0 || i != 0))
                    nearbyPoints.add(new Point(p.x + i, p.y + j));
            }
        }
        return nearbyPoints;
    }
    /*PRIVATE*/

    /* Se fija en todos los puntos pegados, si no hay un enemigo o si son diagonales los quita */
    private ArrayList<Point> validAttackPoints(Point p, Soldier s) {
        ArrayList<Point> validAttackPoints = nearbyPoints(p);
        Iterator<Point> iterator = validAttackPoints.iterator();
        while (iterator.hasNext()) {
            Point z = iterator.next();
            if ((z.x != p.x && z.y != p.y) || !areEnemies(s, getSoldier(z))) {
                iterator.remove();
            }
        }
        return validAttackPoints;
    }

    public void addSoldier(Soldier s, Point p) {
        board[p.x][p.y] = s;
        game.registerAction(new pendingDrawing(null, p, s, 0));
    }

    public ArrayList<Soldier> affectedBySpell(Point p) {
        ArrayList<Point> spellArea = nearbyPoints(p);
        ArrayList<Soldier> affectedBySpell = new ArrayList<>();

        Iterator<Point> iterator = spellArea.iterator();
        while (iterator.hasNext()) {

            Soldier s = getSoldier(iterator.next());
            if( s != null )
                affectedBySpell.add(s);
        }

        return affectedBySpell;
    }

    public Soldier getSoldier(Point p) {
        return board[p.x][p.y];
    }

    /* Se fija en todos los puntos pegados (incluido diagonales), lo quita si:
        - Hay un monstruo
        - Se fija en los puntos si son diagonales y es un heroe. Si NO cumple esto lo quita.
    Luego agrega el punto de retirada.
     */
    public HashMap<Point, Boolean> validMovePoints(Point p, Player playedBy) {
        Soldier soldierAtP = getSoldier(p);
        if (playedBy != game.getCurrentPlayer() || soldierAtP == null || soldierAtP.getOwner() != playedBy)
            return new HashMap<Point, Boolean>();

        ArrayList<Point> validMovePoints = nearbyPoints(p);
        Iterator<Point> iterator = validMovePoints.iterator();
        while (iterator.hasNext()) {
            Point z = iterator.next();
            if ((z.x != p.x && z.y != p.y && !(soldierAtP instanceof Hero)) || getSoldier(z) != null)
                iterator.remove();
        }

        int closerToBase = soldierAtP.getOwner().getCastleRow() == 0 ? -1 : 1;
        Point surrenderPoint = new Point(p.x + closerToBase, p.y);

        if (validMovePoints.contains(surrenderPoint) && !validAttackPoints(p, soldierAtP).isEmpty()) {
            surrenderPoint.translate(closerToBase, 0);
            if (isPointValid(surrenderPoint.x, surrenderPoint.y) && board[surrenderPoint.x][surrenderPoint.y] == null)
                validMovePoints.add(surrenderPoint);
        }

        HashMap<Point, Boolean> validMoveMapPoints = new HashMap<>();
        for (Point z : validMovePoints) {
            if (!validAttackPoints(z, soldierAtP).isEmpty())
                validMoveMapPoints.put(z, true);
            else
                validMoveMapPoints.put(z, false);
        }

        return validMoveMapPoints;
    }

    /* Despues vemos si la necesitamos
    public HashMap<Point, Soldier> returnToMap() {
        HashMap<Point,Soldier> map= new HashMap<>();
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if(board[i][j] != null)
                    map.put(new Point(i,j), board[i][j]);
            }
        }
        return map;
    }*/

    /*ESTO NO SE SI LO TIENE QUE HACER GAME O BOARD, me daria lo mismo pero hay que ponerlo donde tiene que ir.
    Se fija en todos los puntos validos de ataque cual es el de menor vida
     */
    public Soldier enemyToAttack(Point p) {
        ArrayList<Point> validAttackPoints = validAttackPoints(p, getSoldier(p));
        Soldier SoldierToAttack = null;
        for (Point x : validAttackPoints) {
            if (areEnemies(getSoldier(p), getSoldier(x)))
                if (SoldierToAttack == null || SoldierToAttack.getHealth() > getSoldier(x).getHealth())
                    SoldierToAttack = getSoldier(x);
        }
        return SoldierToAttack;
    }

    public void moveSoldier(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
        game.registerAction(new pendingDrawing(origin, dest, getSoldier(origin), 0));
    }

    public void removeDeadFromBoard(Soldier s) {
        Point p = searchSoldier(s);
        board[p.x][p.y] = null;
    }

    public Point searchSoldier(Soldier s) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == s)
                    return new Point(i, j);
            }
        }
        return null; //Meter una excepcion
    }

    public ArrayList<Point> availableSpawns(Player currentPlayer, Card c) {
        ArrayList<Point> availablePoints = new ArrayList<>();

        if(c instanceof Soldier) {
            int spawnRow = currentPlayer.getCastleRow();
            for (int j = 0; j < 7; j++)
                if (board[spawnRow][j] == null)
                    availablePoints.add(new Point(spawnRow, j));
        } else if(c instanceof Magic) {

            Hero h = currentPlayer.getHero();
            if( h != null)
                availablePoints.add(game.searchSoldier(h));
        }

        return availablePoints;

    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(board);
    }

    public void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        board = (Soldier[][]) ois.readObject();
    }
}