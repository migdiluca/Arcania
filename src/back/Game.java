package back;

import java.awt.*;
import java.util.ArrayList;

public class Game {
    private Soldier[7][7] board;
    private Player[2] players;
    /* * test * / */

    public static final int castleLife = 1000;

    public Game() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++)
                tablero[i][j] = null;
        }
        players[0] = new Player(, castleLife); //faltan definir las cartas
        players[1] = new Player(, castleLife);

    }

    public void attackCastle() {
        for(int i = 0 ; i < 7; i++) {
            if (board[0][i] != null) {
                players[1].getCastle().getAttacked(board[0][i].getAttackPoint);
            }
        }
    }

    private boolean isPointValid(int x, int y) {
        if (x >= 0 && x < 7 && y >= 0 && y < 7 && board[x][y] == null)
            return true;
        return false;
    }

    public ArrayList<Point> canMove (Point p) {
        ArrayList<Point> posibleMovements = new ArrayList<Point>();
        if(board[p.getX()][p.getY()] == null)
            return posibleMovements;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j != 0 || i != 0) {
                    if (isPointValid(p.getX() + i, p.getY() + j)) {
                        if (j != 0 && i != 0) {
                            if (board[p.getX()][p.getY()] instanceof heroe) {
                                Point posible = new Point(p.getX() + i, p.getY() + j);
                                posibleMovements.add(posible);
                            }
                        } else {
                            Point posible = new Point(p.getX() + i, p.getY() + j);
                            posibleMovements.add(posible);
                        }
                    }
                }
            }
        }
        return posibleMovements;
    }

    public void moveSoldier (Point origin, Point dest) {
        board[dest.getX()][dest.getY()] = board[origin.getX()][origin.getY()];
        board[origin.getX()][origin.getY()] == null;
    }
}
