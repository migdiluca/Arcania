package back;

import java.awt.*;
import java.util.ArrayList;

public class Board {
    private Soldier board[][];

    public Board() {
        board = new Soldier[7][7];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = null;
    }

    public void addSoldier(Soldier soldier, Point p) {
        board[ p.x][p.y] = soldier;
    }

    // Los dos no ven su equipo del lado de abajo no?
    public ArrayList<Point> availableSpawns() {
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        for (int j = 0; j < 8; j++)
            if (board[0][j] == null)
                availablePoints.add(new Point(0, j));
        return availablePoints;
    }

    private boolean isPointValid(int x, int y) {
        if (x >= 0 && x < 7 && y >= 0 && y < 7 && board[x][y] == null)
            return true;
        return false;
    }

    public ArrayList<Point> canMove(Point p) {
        ArrayList<Point> posibleMovements = new ArrayList<Point>();
        if (board[p.x][p.y] == null)
            return posibleMovements;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j != 0 || i != 0) {
                    if (isPointValid(p.x + i, p.y + j)) {
                        if (j != 0 && i != 0) {
                            if (board[p.x][p.y] instanceof heroe) {
                                Point posible = new Point(p.x + i, p.y + j);
                                posibleMovements.add(posible);
                            }
                        } else {
                            Point posible = new Point(p.x + i, p.y + j);
                            posibleMovements.add(posible);
                        }
                    }
                }
            }
        }
        return posibleMovements;
    }

    public void moveSoldier(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    /* En el tablero no me puedo fijar si son enemigos, podriamos hacer que board sea una matriz de
    * un arreglo que tenga el soldado y el jugador*/

    private Point searchForEnemy(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((i == 0 || j == 0) && board[x+i][y+j] != null) {
                    if(board[x][y] == )
                }
            }
        }
    }

    public ArrayList<Point> startBattle() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {

                }
            }
        }
    }
}