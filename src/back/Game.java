package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Game {
    private Player players[];
    private Stack<Card> allCards;
    private Monster board[][];

    public Game() {
        board = new Monster[7][7];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = null;
        players = new Player[2];
       /* players[0] = new Player();
        players[1] = new Player();*/
    }

    private void removeDead(Monster monster) {
        Point p = searchMonster(monster);
        players[0].aliveCards.remove(monster);
        players[1].aliveCards.remove(monster);
        board[p.x][p.y] = null;
    }

    public int endTurn(int last, int other) {
        attack(players[last].aliveCards);
        attack(players[other].aliveCards);
        if(players[other].castle.getLife() <= 0 || !players[other].canPlay())
            return 0;
        if(players[last].castle.getLife() <= 0 || !players[last].canPlay())
            return 1;
        return -1;
    }

    public void addMonster(Monster m, Point p) {
        board[ p.x][p.y] = m;
        //falta remover el mounstro de la mano del jugador
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
        if (x >= 0 && x < 7 && y >= 0 && y < 7)
            return true;
        return false;
    }

    public ArrayList<Point> canMove(Point p) {
        ArrayList<Point> posibleMovements = validPoints(p);
        for(Point x: posibleMovements) {
            if(board[x.x][x.y] != null)
                posibleMovements.remove(x);
        }
        return posibleMovements;
    }

    public void moveMonster(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    /* En el tablero no me puedo fijar si son enemigos, podriamos hacer que board sea una matriz de
    * un arreglo que tenga el soldado y el jugador*/

    private boolean areEnemies(Monster m1, Monster m2) {
        if(m2 == null)
            return false;
        if(players[0].aliveCards.contains(m1) && !players[0].aliveCards.contains(m2))
            return true;
        else if(players[0].aliveCards.contains(m2) && !players[0].aliveCards.contains(m1))
            return true;
        return false;
    }

    private ArrayList<Point> validPoints(Point p) {
        ArrayList<Point> validPoints = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isPointValid(p.x +i, p.y+j) && (j != 0 || i != 0))
                    if ((j != 0 && i != 0 && board[p.x][p.y] instanceof Heroe) || j == 0 || i == 0)
                        validPoints.add(new Point(p.x+i, p.y+j));
            }
        }
        return validPoints;
    }

    private Point searchMonster(Monster m) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == m)
                    return new Point(i,j);
            }
        }
        return null; //esto es medio turbio, lo hice porque me pide un return, pero al mounstro siempre lo encuentra
    }

    private void attack(ArrayList<Monster> monsters) {
        for (Monster m : monsters) {
            Point attackerPosition = searchMonster(m);
            if(attackerPosition.x == 6) {
                if(players[0].aliveCards.contains(m))
                    m.attackCastle(players[1].castle);
                else
                    m.attackCastle(players[0].castle);
            }
            else {
                ArrayList<Point> validPoints = validPoints(attackerPosition);
                for (int i = 0, flag = 0; i < validPoints.size() && flag == 0; i++) {
                    if (areEnemies(m, board[validPoints.get(i).x][validPoints.get(i).y])) {
                        m.attack(board[validPoints.get(i).x][validPoints.get(i).y]);
                        if (board[validPoints.get(i).x][validPoints.get(i).y].getHealth() <= 0)
                            removeDead(board[validPoints.get(i).x][validPoints.get(i).y]);
                        flag = 1;
                    }
                }
            }
        }
    }
}
