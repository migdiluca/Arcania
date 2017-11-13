package back;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Game implements Serializable{
    private Player player1;
    private Player player2;
    protected Board board; /*esta protected para que directamente puedan hacer game.board.getPoints
                             para tomar puntos de spawn etc..*/
    private Player currentPlayer;
    private int actionsLeft;

    public Game(String player1Name, String player2Name) {
        board = new Board(this);
        player1 = new Player(player1Name, createDeck(),6);
        player2 = new Player(player2Name, createDeck(), 0);
        player1.cardsToHand(5);
        player2.cardsToHand(5);

        Hero h1 = new Hero ("Avatar de la Oscuridad", 2, 10,80,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo.");
        h1.setOwner(player1);

        Hero h2 = new Hero ("Avatar de la Oscuridad", 2, 10,80,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo.");
        h2.setOwner(player2);

        /* esto no va a ser asi, es para testear */
        currentPlayer = player1;
        addSoldier(h1,new Point(player1.getCastleRow(), 3));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 4));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 5));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 1));
        addSoldier((Soldier)player1.hand.get(0), new Point(player1.getCastleRow(), 0));

        currentPlayer = player2;
        addSoldier(h2,new Point(player2.getCastleRow(), 3));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 6));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 4));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 0));
        addSoldier((Soldier)player2.hand.get(0), new Point(player2.getCastleRow(), 1));
        currentPlayer = player1;
        actionsLeft = 5;
    }

    private void removeDead(Soldier s) {
        player1.aliveCards.remove(s);
        player2.aliveCards.remove(s);
        registerAction(new pendingDrawing(board.searchSoldier(s), null, s, 0));
        board.removeDeadFromBoard(s);
    }

    private ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Caballero Negro", 1, 10,80,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Ogro", 3, 30,60,5,15, "Bestias de gran fuerza física. Leales por sobre todas las cosas, los Ogros que se prestan a tu causa lucharán hasta el último aliento... de sus enemigos."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Gorgona", 4, 20,45,6,45, "Bestia antigua de tiempos ya olvidados, la Gorgona se destaca por su celeridad y su mordida siempre certera."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Guerrero Orco", 5, 25,50,8,20, "Incluso con la extinción al acecho, los orcos no le escapan a la batalla y a la posibilidad de grabar sus nombres en la historia."));

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

    protected void registerAction(pendingDrawing pd) {
        getPlayer1().registerAction(pd);
        getPlayer2().registerAction(pd);
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
                    if(s.attack(m2) == 1) {
                        registerAction(new pendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, 1));
                        if(!m2.isAlive()) {
                            removeDead(m2);
                        }

                    } else {
                        registerAction(new pendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, 2));
                    }

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

    /* Aca no se si playedBy se lo tenes que pasar porque los jugadores los tiene game
    pero no me acuerdo porque era playedBy y no currentPlayer
     */
    public HashMap<Point, Boolean> validMovePoints(Point p, Player windowOwner) {
        if(windowOwner != currentPlayer) {
            return new HashMap<>();
        }
        return board.validMovePoints(p,currentPlayer);
    }

    public Soldier getSoldier(Point p) {
        return board.getSoldier(p);
    }

    public void addSoldier(Soldier s, Point p) {
        if(s.getOwner() == currentPlayer) {
            invokeSoldier(s, p);
            currentPlayer.playCard(s);
        }
    }

    public void invokeSoldier(Soldier s, Point p) {
        board.addSoldier(s, p);
        actionsLeft--;
    }

    public void moveSoldier(Point origin, Point dest) {
        board.moveSoldier(origin,dest);
        actionsLeft--;
    }

    /* no se si corroborar que es el current player por como se llamaria desde el front */
    public void flipCard(Player player) {
        if(player == currentPlayer) {
            currentPlayer.cardsToHand();
            actionsLeft--;
        }
    }

    public Point searchSoldier(Soldier s) {
        return board.searchSoldier(s);
    }

    public ArrayList<Point> availableSpawns() {
        return board.availableSpawns(currentPlayer);
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
        actionsLeft = 5;
        return null;
    }

}
