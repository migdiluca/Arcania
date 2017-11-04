package back;

import java.awt.*;
import java.util.ArrayList;

public class Board {
    private Monster board[][];

    public Board() {
        board = new Monster[7][7];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                board[i][j] = null;
    }

    public void addMonster(Monster m, Point p) {
        board[p.x][p.y] = m;
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
        ArrayList<Point> posibleMovements = validMovePoints(p);
        for (Point x : posibleMovements) {
            if (board[x.x][x.y] != null)
                posibleMovements.remove(x);
        }
        return posibleMovements;
    }

    public boolean canAttackCastle(Point p, int playerNumber){
        int attackRow = playerNumber == 0 ? 6 : 0;
        if(p.x == attackRow)
            return true;
        return false;
    }

    private boolean areEnemies(Monster m1, Monster m2) {
        if(m2 == null)
            return false;
        if(m1.getOwner() != m2.getOwner())
            return true;
        return false;
    }

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

    private ArrayList<Point> nearbyPoints(Point p) {
        ArrayList<Point> nearbyPoints = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isPointValid(p.x +i, p.y+j) && (j != 0 || i != 0))
                    nearbyPoints.add(new Point(p.x+i, p.y+j));
            }
        }
        return nearbyPoints;
    }

    private ArrayList<Point> validMovePoints(Point p) {
        ArrayList<Point> validMovePoints = nearbyPoints(p);
        for(Point z : validMovePoints) {
            if (!((z.x != p.x && z.y != p.x && getMonster(p) instanceof Heroe) || (z.x == 0 || z.y == 0)))
                validMovePoints.remove(z);
        }
        return validMovePoints;
    }

    private ArrayList<Point> validAttackPoints(Point p) {
        ArrayList<Point> validAttackPoints = validMovePoints(p);
        for(Point z : validAttackPoints) {
            if((z.x != p.x && z.y != p.y) || !areEnemies(getMonster(p), getMonster(z)))
                validAttackPoints.remove(z);
        }
        return validAttackPoints;
    }

    public void moveMonster(Point origin, Point dest) {
        board[dest.x][dest.y] = board[origin.x][origin.y];
        board[origin.x][origin.y] = null;
    }

    public void removeDeadFromBoard(Monster m) {
        Point p = searchMonster(m);
        board[p.x][p.y] = null;
    }

    public Monster getMonster(Point p) {
        return board[p.x][p.y];
    }

    public Point searchMonster(Monster m) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == m)
                    return new Point(i,j);
            }
        }
        return null; //esto es medio turbio, lo hice porque me pide un return, pero al mounstro siempre lo encuentra
    }

    public ArrayList<Point> availableSpawns(int playerNumber) {
        ArrayList<Point> availablePoints = new ArrayList<Point>();
        int spawnRow = playerNumber == 0 ? 0 : 6;
        for (int j = 0; j < 8; j++)
            if (board[spawnRow][j] == null)
                availablePoints.add(new Point(spawnRow, j));
        return availablePoints;
    }

}