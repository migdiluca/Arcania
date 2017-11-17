package back;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Tablero del juego que contiene los jugadores vivos.
 */
public class Board implements Serializable {
    private Soldier board[][];
    private static final long serialVersionUID = 1L;

    /**
     * Crea una matriz de 7x7 y en todos los lugares le asigna null
     */
    public Board() {
        board = new Soldier[7][7];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                board[i][j] = null;
    }

    /**
     * Se fija si el punto esta en la matriz.
     * @param x Punto en x a analizar
     * @param y Punto en y a analizar.
     * @return True si el punto pertenece a la matriz, false en caso contrario.
     */
    private boolean isPointValid(int x, int y) {
        return (x >= 0 && x < 7 && y >= 0 && y < 7);
    }

    /**
     * Analiza si dos soldados son enemigos.
     * @param s1 Soldado uno.
     * @param s2 Soldado dos.
     * @return True si son enemigos, false en caso contrario.
     */
    private boolean areEnemies(Soldier s1, Soldier s2) {
        if (s1 == null || s2 == null)
            return false;
        return s1.getOwner() != s2.getOwner();
    }

    /**
     * Devuelve todos los puntos (diagonal incluido) aledaños al punto p, comprobando
     que sean validos
     * @param p Punto a analizar.
     * @return ArrayList con los puntos que cumplen.
     */
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

    /**
     * Se fija en las posiciones aledañas a un punto, sin incluir las diagonales, si hay un enemigo del un soldado.
     * @param p Punto a partir del cual verifica las posiciones aledañas.
     * @param s Soldado a partir del cual se fija si los encontrados son o no enemigos.
     * @return ArrayList con los puntos en los que hay enemigos del soldado s.
     */
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

    /**
     * Agrega un soldado al tablero.
     * @param s Soldado a agregar.
     * @param p Punto en el cual agrega el soldado.
     */
    public void addSoldier(Soldier s, Point p) {
        board[p.x][p.y] = s;

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

    /**
     * Toma un soldado a partir de un punto.
     * @param p Punto en el que se fija si hay o no un soldado, si hay lo devuelve.
     * @return Soldado en el punto p.
     */
    public Soldier getSoldier(Point p) {
        return board[p.x][p.y];
    }

    /**
     * Se fija las posibles posiciones en las que puede mover un soldado en el punto p, ademas
     * analiza si esa posicion desencanedara un ataque o no.
     * @param p Punto a partir del cual analiza los posibles movimientos.
     * @return HashMap con los puntos en los cuales se puede mover en key y en value contiene true si
     * el punto al que se mueve desencadena un combate, false en caso contrario.
     */
    public HashMap<Point, Boolean> validMovePoints(Point p) {
        Soldier soldierAtP = getSoldier(p);

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

    /**
     * Verifica cual es el enemigo con menos vida al que puede atacar un soldado ubicado en el punto p.
     * @param p Punto en el cual se ubica el atacante.
     * @return Soldado al que debe atacar.
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

    /**
     * Mueve al soldado de un punto a otro en la matriz.
     * @param origin Punto de origen.
     * @param dest Punto de destino.
     */
    public void moveSoldier(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    /**
     * Remueve un soldado muerto del tablero.
     * @param s Soldado a remover.
     */
    public void removeDeadFromBoard(Soldier s) {
        Point p = searchSoldier(s);
        board[p.x][p.y] = null;
    }

    /**
     * Busca un soldado en la matriz.
     * @param s Soldado a buscar.
     * @return Punto en el cual se encuentra el soldado.
     */
    public Point searchSoldier(Soldier s) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == s)
                    return new Point(i, j);
            }
        }
        return null;
    }

    /**
     * Busca los posibles lugares en los cuales puede utilizarse una carta magica o invocar
     * un nuevo soldado.
     * @param currentPlayer Jugador que invoca la carta.
     * @param c Carta a invocar.
     * @return ArrayList con los puntos posibles a invocar la carta.
     */
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
                availablePoints.add(searchSoldier(h));
        }

        return availablePoints;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(board);
    }

    private void readObject(ObjectInputStream ois) throws IOException , ClassNotFoundException {
        ois.defaultReadObject();
        board = (Soldier[][]) ois.readObject();
    }
}