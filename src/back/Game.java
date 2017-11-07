package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Game {
    private Player player1;
    private Player player2;
    private Stack<Card> allCards; //no se si se necesita, despues veo
    protected Board board; //esta protected para que directamente puedan hacer game.board.getPoints
                            // para tomar puntos de spawn etc..
    private Player currentPlayer;

    public Game() {
        Board board = new Board();
       /* players[0] = new Player();
        players[1] = new Player();*/
        currentPlayer = player1;
    }

    private void removeDead(Soldier m) {
        player1.aliveCards.remove(m);
        player2.aliveCards.remove(m);
        board.removeDeadFromBoard(m);
    }

    private Castle canAttackCastle(Soldier m){
        Point attackerPosition = board.searchSoldier(m);
        if((attackerPosition.x == player1.getCastleRow() && m.getOwner() != player1))
            return player1.castle;
        else if((attackerPosition.x == player2.getCastleRow() && m.getOwner() != player2))
            return player2.castle;
        return null;
    }

    /* Para todos los monstruos del jugador me fijo su posicion y primero si puede atacar al castillo lo ataca.
    Si no puede, en el tablero se comprueba que exista a quien atacar y lo ataca, si muere el otro lo remueve
     */
    private void performAttack(ArrayList<Soldier> monsters) {
        for (Soldier m : monsters) {
            Castle castleToAttack = canAttackCastle(m);
            if(castleToAttack != null)
                m.attackCastle(castleToAttack);
            else {
                Soldier m2 = board.enemyToAttack(board.searchSoldier(m));
                if(m2 != null) {
                    m.attack(m2);
                    if(!m2.isAlive())
                        removeDead(m2);
                }
            }
        }
    }

    public void addSoldier(Soldier m, Point p) {
        board.addSoldier(m , p);
        if(!player1.playCard(m))
            player2.playCard(m);
    }

    /* Hace los ataques en orden, se fija si gano alguno y despues cambia el turno */
    public Player endTurn() {
        performAttack(currentPlayer.aliveCards);
        Player otherPlayer;
        otherPlayer = currentPlayer == player1 ? player2 : player1;
        performAttack(otherPlayer.aliveCards);

        if(otherPlayer.castle.getLife() <= 0 || !otherPlayer.canPlay())
            return currentPlayer;
        if(currentPlayer.castle.getLife() <= 0 || !currentPlayer.canPlay())
            return otherPlayer;

        currentPlayer = otherPlayer;
        return null;
    }

}
