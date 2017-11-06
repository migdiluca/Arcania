package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Game {
    private Player players[];
    private Stack<Card> allCards; //no se si se necesita, despues veo
    protected Board board; //esta protected para que directamente puedan hacer game.board.getPoints
                            // para tomar puntos de spawn etc..
    private int turn;

    public Game() {
        turn = players[0].getPlayerNumber();
        Board board = new Board();
        players = new Player[2];
       /* players[0] = new Player();
        players[1] = new Player();*/
    }

    private void removeDead(Monster m) {
        players[0].aliveCards.remove(m);
        players[1].aliveCards.remove(m);
        board.removeDeadFromBoard(m);
    }

    /* Para todos los monstruos del jugador me fijo su posicion y primero si puede atacar al castillo lo ataca.
    Si no puede, en el tablero se comprueba que exista a quien atacar y lo ataca, si muere el otro lo remueve
     */
    private void performAttack(ArrayList<Monster> monsters) {
        for (Monster m : monsters) {
            Point attackerPosition = board.searchMonster(m);
            /*if(board.canAttackCastle(attackerPosition, m.getOwnerNumber())) {
                int enemyNumber = m.getOwnerNumber() == 0 ? 1 : 0;
                m.attackCastle(players[enemyNumber].castle);
            }
            else {
                Monster m2 = board.enemyToAttack(attackerPosition);
                if(m2 != null) {
                    m.attack(m2);
                    if(!m2.isAlive())
                        removeDead(m2);
                }
            }*/
        }
    }

    public void addMonster(Monster m, Point p) {
        board.addMonster(m , p);
        if(!players[0].playCard(m))
            players[1].playCard(m);
    }

    /* Hace los ataques en orden, se fija si gano alguno y despues cambia el turno */
    public int endTurn() {
        performAttack(players[turn].aliveCards);
        int other;
        other = turn == players[0].getPlayerNumber() ? players[1].getPlayerNumber() : players[0].getPlayerNumber();
        performAttack(players[other].aliveCards);

        if(players[other].castle.getLife() <= 0 || !players[other].canPlay())
            return turn;
        if(players[turn].castle.getLife() <= 0 || !players[turn].canPlay())
            return other;

        turn = other;
        return -1;
    }

}
