package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Game {
    private Player player1;
    private Player player2;
    protected Board board; //esta protected para que directamente puedan hacer game.board.getPoints
                            // para tomar puntos de spawn etc..
    private Player currentPlayer;

    public Game(String player1Name, String player2Name) {
        board = new Board();
        ArrayList<Card> deck = createDeck();
        player1 = new Player(player1Name, deck,6);
        Collections.shuffle(deck);
        player2 = new Player(player2Name, deck, 0);
        currentPlayer = player1;
    }

    private void removeDead(Soldier s) {
        player1.aliveCards.remove(s);
        player2.aliveCards.remove(s);
        board.removeDeadFromBoard(s);
    }

    private ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();;
        Soldier s1 = new Soldier("Soldado", 3,5,6,7);
        deck.add(s1);
        Soldier s2 = new Soldier("Soldado", 3,5,6,7);
        deck.add(s2);
        return deck;
    }

    private Castle canAttackCastle(Soldier s){
        Point attackerPosition = board.searchSoldier(s);
        if((attackerPosition.x == player1.getCastleRow() && s.getOwner() != player1))
            return player1.castle;
        else if((attackerPosition.x == player2.getCastleRow() && s.getOwner() != player2))
            return player2.castle;
        return null;
    }

    /* Para todos los monstruos del jugador me fijo su posicion y primero si puede atacar al castillo lo ataca.
    Si no puede, en el tablero se comprueba que exista a quien atacar y lo ataca, si muere el otro lo remueve
     */
    private void performAttack(ArrayList<Soldier> soldiers) {
        for (Soldier s : soldiers) {
            Castle castleToAttack = canAttackCastle(s);
            if(castleToAttack != null)
                s.attackCastle(castleToAttack);
            else {
                Soldier m2 = board.enemyToAttack(board.searchSoldier(s));
                if(m2 != null) {
                    s.attack(m2);
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
    public String endTurn() {
        performAttack(currentPlayer.aliveCards);
        Player otherPlayer = currentPlayer == player1 ? player2 : player1;
        performAttack(otherPlayer.aliveCards);

        if(otherPlayer.castle.getLife() <= 0 || !otherPlayer.canPlay())
            return currentPlayer.getName();
        if(currentPlayer.castle.getLife() <= 0 || !currentPlayer.canPlay())
            return otherPlayer.getName();

        currentPlayer = otherPlayer;
        return null;
    }

}
