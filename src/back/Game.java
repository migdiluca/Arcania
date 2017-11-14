package back;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Game implements Serializable{
    private Player player1;
    private static final long serialVersionUID = 1L;
    private Player player2;
    private Board board; /*esta protected para que directamente puedan hacer game.board.getPoints
                             para tomar puntos de spawn etc..*/
    private Player currentPlayer;
    private int actionsLeft;

    public Game(String player1Name, String player2Name) {
        board = new Board();
        player1 = new Player(player1Name, createDeck(),6);
        player2 = new Player(player2Name, createDeck(), 0);
        for(int i = 0; i < 5; i++) {
            player1.cardsToHand();
            player2.cardsToHand();
        }

        Hero h1 = new Hero ("Avatar de la Oscuridad", 2, 10,80,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo.");
        h1.setOwner(player1);

        Hero h2 = new Hero ("Avatar de la Oscuridad", 2, 10,80,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo.");
        h2.setOwner(player2);

        /* esto no va a ser asi, es para testear */
        currentPlayer = player1;

        player1.playSoldier(h1);
        player2.playSoldier(h2);

        board.addSoldier(h1,new Point(player1.getCastleRow(), 3));

        currentPlayer = player2;
        board.addSoldier(h2,new Point(player2.getCastleRow(), 3));

        currentPlayer = player1;
        actionsLeft = 5;
    }

    private void removeDead(Soldier s) {
        player1.aliveCards.remove(s);
        player2.aliveCards.remove(s);
        registerAction(new pendingDrawing(board.searchSoldier(s), null, s, ActionType.MOVEMENT));
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

        for(int i = 0; i < 5; i++)
            deck.add(new Heal("Sanación Superior", 5, "Recupera hasta 25 puntos de vida a cada unidad afectada.",false, 25, 0));

        for(int i = 0; i < 5; i++)
            deck.add(new Poison("Envenenar", 5, "Envenena a las unidades aledañas al héroe, inflingiéndo un daño de 10 puntos por turno durante 3 turnos.",false, 10, 3));

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

    private void performAction() {
        actionsLeft--;
        if(actionsLeft == 0)
            endTurn();
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
                        registerAction(new pendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, ActionType.STRIKE));
                        if(!m2.isAlive())
                            removeDead(m2);
                    } else {
                        registerAction(new pendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, ActionType.EVADE));
                    }

                }
            }
        }
    }

    private void registerAction(pendingDrawing pd) {
        getPlayer1().registerAction(pd);
        getPlayer2().registerAction(pd);
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

    public Soldier getSoldier(Point p) {
        return board.getSoldier(p);
    }

    public int getActionsLeft() {
        return actionsLeft;
    }

    public Point searchSoldier(Soldier s) {
        return board.searchSoldier(s);
    }

    public ArrayList<Point> availableSpawns(Card c) {
        return board.availableSpawns(currentPlayer, c);
    }

    public HashMap<Point, Boolean> validMovePoints(Point p, Player windowOwner) {
        Soldier s = board.getSoldier(p);
        if(s == null || windowOwner != currentPlayer || !s.canMove()) {
            return new HashMap<>();
        }
        return board.validMovePoints(p,currentPlayer);
    }

    public void moveSoldier(Point origin, Point dest) {
        Soldier s = board.getSoldier(origin);
        if(s.canMove()) {
            board.moveSoldier(origin, dest);
            s.disableMovement();
            performAction();
            registerAction(new pendingDrawing(origin, dest, getSoldier(origin), ActionType.MOVEMENT));
        }
    }

    public Card flipCard(Player player) {
        Card c = null;
        if(player == currentPlayer) {
            c = currentPlayer.cardsToHand();
            performAction();
        }
        return c;
    }

    public void playCard(Card c, Point p) {
        if( c instanceof Soldier ) {
            board.addSoldier((Soldier) c, p);
            currentPlayer.playSoldier((Soldier) c);
            registerAction(new pendingDrawing(null, p, c, ActionType.MOVEMENT));
        } else if( c instanceof Magic ) {
            ArrayList<Soldier> affectedBySpell = board.affectedBySpell(p);

            for(Soldier s: affectedBySpell)
                s.curse((Magic) c);

        }
        currentPlayer.discardCard(c);
        performAction();
    }

    /* Hace los ataques en orden, se fija si gano alguno y despues cambia el turno */
    public void endTurn() {
        performAttack(currentPlayer.aliveCards);

        for(Soldier s: currentPlayer.aliveCards) {
            s.enableMovement();
            s.applyMagic();
        }

        Player otherPlayer = currentPlayer == player1 ? player2 : player1;
        performAttack(otherPlayer.aliveCards);

        if(otherPlayer.castle.getLife() <= 0 || !otherPlayer.canPlay())
            //agregar pending
        if(currentPlayer.castle.getLife() <= 0 || !currentPlayer.canPlay())
            //agregar pending

        currentPlayer = otherPlayer;
        actionsLeft = 5;
    }

    File file;

    public void writeGame() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(this);
        oos.close();
    }


   public void loadGame() throws IOException, ClassNotFoundException {
       ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
       Game saveGame = (Game) ois.readObject();
       this.currentPlayer = saveGame.currentPlayer;
       this.player1 = saveGame.player1;
       this.player2 = saveGame.player2;
       this.board = saveGame.board;
       ois.close();
   }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(player1);
        out.writeObject(player2);
        out.writeObject(currentPlayer);
        out.writeObject(board);
    }


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        player1 = (Player) ois.readObject();
        player2 = (Player) ois.readObject();
        currentPlayer = (Player) ois.readObject();
        board = (Board) ois.readObject();
    }

}
