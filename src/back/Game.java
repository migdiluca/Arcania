package back;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

public class Game implements Serializable{
    private Player player1;
    private Player player2;
    protected Board board; /*esta protected para que directamente puedan hacer game.board.getPoints
                             para tomar puntos de spawn etc..*/
    private Player currentPlayer;

    public Game(String player1Name, String player2Name) {
        board = new Board();
        player1 = new Player(player1Name, createDeck(),6);
        player2 = new Player(player2Name, createDeck(), 0);
        player1.cardsToHand(5);
        player2.cardsToHand(5);
        /* esto no va a ser asi, es para testear */
        currentPlayer = player1;
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 4));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 5));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 1));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 0));
        currentPlayer = player2;
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 3));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 4));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 0));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 1));
        currentPlayer = player1;
    }

    private void removeDead(Soldier s) {
        player1.aliveCards.remove(s);
        player2.aliveCards.remove(s);
        board.removeDeadFromBoard(s);
    }

    private ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Caballero Negro", 1, 15,50,6,7));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Ogro", 2, 15,50,6,7));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Gorgona", 3, 15,50,6,7));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Guerrero Esqueleto", 4, 15,50,6,7));

        Collections.shuffle(deck);
        return deck;
    }

    private Castle castleToAttack(Soldier s){
        Point attackerPosition = board.searchSoldier(s);
        Player enemy = s.getOwner() == player1 ? player2 : player1;

        if(attackerPosition.x == enemy.getCastleRow())
            return enemy.castle;
        return null;
    }

    /* Para todos los monstruos del jugador me fijo su posicion y primero si puede atacar al castillo lo ataca.
    Si no puede, en el tablero se comprueba que exista a quien atacar y lo ataca, si muere el otro lo remueve
     */
    private void performAttack(ArrayList<Soldier> soldiers) {
        for (Soldier s : soldiers) {
            Castle castleToAttack = castleToAttack(s);
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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public HashMap<Point, Boolean> askPosibleMovements(Point p) {
        return board.validMovePoints(p, currentPlayer);
    }

    public Board getBoard() {
        return board;
    }

    public void addSoldier(Soldier s, Point p) {
        if(s.getOwner() == currentPlayer) {
            board.addSoldier(s, p);
            currentPlayer.playCard(s);
        }
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
