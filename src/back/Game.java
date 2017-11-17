package back;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Clase administradora del juego.
 */
public class Game implements Serializable {
    private Player player1;
    private static final long serialVersionUID = 1L;
    private Player player2;
    private Board board;
    private Player currentPlayer;
    private int actionsLeft;


    /**
     * Crea una nueva instancia de Board y de los dos jugadores. Asigna las cartas
     * al mazo de ambos jugadores. Crea los heroes, los asigna a ambos jugadores, y
     * al tablero. Asigna como jugador actual al jugador uno y le establece cinco acciones.
     * @param player1Name Nombre del jugador uno.
     * @param player2Name Nombre del jugador dos.
     */
    public Game(String player1Name, String player2Name) {
        board = new Board();
        player1 = new Player(player1Name, createDeck(),6);
        player2 = new Player(player2Name, createDeck(), 0);
        for(int i = 0; i < 5; i++) {
            player1.cardToHand();
            player2.cardToHand();
        }

        Hero h1 = new Hero ("Avatar de la Oscuridad (Héroe)", 2, 30,120,20,25, "Antaño príncipe de un próspero imperio, es ahora recipiente de un poder mucho más antiguo.");
        h1.setOwner(player1);

        Hero h2 = new Hero ("Avatar de la Oscuridad (Héroe)", 2, 30,120,20,25, "Antaño príncipe de un próspero imperio, es ahora recipiente de un poder mucho más antiguo.");
        h2.setOwner(player2);

        player1.playSoldier(h1);
        player2.playSoldier(h2);

        board.addSoldier(h1,new Point(player1.getCastleRow(), 3));
        board.addSoldier(h2,new Point(player2.getCastleRow(), 3));

        currentPlayer = player1;
        actionsLeft = 5;
        currentPlayer.registerAction(new PendingDrawing(null, null, null, ActionType.STARTTURN));
        player2.registerAction(new PendingDrawing(null, null, null, ActionType.ENDTURN));
    }


    /**
     * Remueve al soldado s de las cartas vivas de su dueño y del tablero.
     * @param s Soldado a remover
     */
    private void removeDead(Soldier s) {
        s.getOwner().getAliveCards().remove(s);
        registerAction(new PendingDrawing(board.searchSoldier(s), null, s, ActionType.MOVEMENT));
        board.removeDeadFromBoard(s);
    }


    /**
     * Crea todas las cartas que se utilizaran en el juego.
     * @return ArrayList con todas las cartas.
     */
    private ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<>();

        for(int i = 0; i < 1; i++)
            deck.add(new Soldier("Caballero Negro", 1, 10,85,20,0, "Resguardado de todo daño por su monumental coraza, el Caballero Negro es capaz de avanzar por el campo absorbiendo el daño enemigo."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Ogro", 3, 40,40,5,15, "Bestias de gran fuerza física. Leales por sobre todas las cosas, los Ogros que se prestan a tu causa lucharán hasta el último aliento... de sus enemigos."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Gorgona", 4, 20,50,6,45, "Bestia antigua de tiempos ya olvidados, la Gorgona se destaca por su celeridad y su mordida siempre certera."));

        for(int i = 0; i < 5; i++)
            deck.add(new Soldier("Guerrero Orco", 5, 25,55,8,20, "Incluso con la extinción al acecho, los orcos no le escapan a la batalla y a la posibilidad de grabar sus nombres en la historia."));

        for(int i = 0; i < 3; i++)
            deck.add(new Heal("Sanación Superior", 501, "Recupera hasta 25 puntos de vida a cada unidad afectada.",false, 25, 0));

        for(int i = 0; i < 4; i++)
            deck.add(new MagicDamage("Tormenta de Fuego", 504, "Inflinge 25 puntos de daño a las unidades aledañas.",true, 25));

        deck.add(new MagicDamage("Juicio", 502, "Inflinge 40 puntos de daño a las unidades aledañas.",true, 40));

        for(int i = 0; i < 3; i++)
            deck.add(new Poison("Envenenar", 503, "Envenena a las unidades aledañas al héroe, inflingiéndo un daño de 10 puntos por turno durante 3 turnos.",true, 10, 3));

        for(int i = 0; i < 4; i++)
            deck.add(new Inspiration("Inspiración", 505, "Aumenta en 5 puntos el ataque durante 5 turnos.",false, 5));

        Collections.shuffle(deck);
        return deck;
    }


    /**
     * Se fija si el jugador puede atacar al castillo contrario.
     * @param s Soldado que realizara el ataque.
     * @return Retorna el castillo a atacar, o nulo si no puede atacar un castillo.
     */
    private Castle castleToAttack(Soldier s){
        Point attackerPosition = board.searchSoldier(s);
        Player enemy = s.getOwner() == player1 ? player2 : player1;

        if(attackerPosition.x == enemy.getCastleRow())
            return enemy.getCastle();
        return null;
    }


    /**
     * Reduce las acciones restantes y si llego a cero finaliza el turno.
     */
    private void performAction() {
        actionsLeft--;
        if(actionsLeft == 0)
            endTurn();
    }


    /**
     * Para todos los soldados en el arreglo se fija si pueden atacar el castillo enemigo,
     * si pueden lo atacan y pasa al siguiente, si no puede busca a que soldado debe atacar.
     * Si tiene soldados para atacar lo ataca y se fija si el mismo murio para removerlo del juego.
     * Si no tiene soldados para atacar no realiza nada.
     * @param soldiers Arreglo con los solados que realizaran sus ataques.
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
                        registerAction(new PendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, ActionType.STRIKE));
                        if(!m2.isAlive())
                            removeDead(m2);
                    } else {
                        registerAction(new PendingDrawing(board.searchSoldier(s), board.searchSoldier(m2), s, ActionType.EVADE));
                    }

                }
            }
        }
    }


    /**
     * Aplica a los soldados afectados el efecto de magia por turno que tengan
     * @param soldiers Lista de soldados que desencadenarán el evento applyMagic
     */
    private void applyMagicToSoldiers(ArrayList<Soldier> soldiers) {
        Iterator<Soldier> iterator = soldiers.iterator();
        while(iterator.hasNext()) {
            Soldier s = iterator.next();
            s.applyMagic();
            if(!s.isAlive()) {
                iterator.remove();
                registerAction(new PendingDrawing(board.searchSoldier(s), null, s, ActionType.MOVEMENT));
                board.removeDeadFromBoard(s);
            }
        }
    }

    /**
     * Indica a los clientes de ambos jugadores que reflejen la acción correspondiente
     * @param pd la PendingAction
     */
    private void registerAction(PendingDrawing pd) {
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


    /**
     * Llama la funcion availableSpawns en board con el jugador actual y la carta recibida.
     * @param c Carta a analizar.
     * @return ArrayList con los posibles lugares a invocar la carta.
     */
    public ArrayList<Point> availableSpawns(Card c) {
        return board.availableSpawns(currentPlayer, c);
    }


    /**
     * Analiza los posibles puntos en los que se puede mover un soldado.
     * Si el soldado es nulo, si el que lo intenta mover no corresponde al jugador actual, si el soldado
     * ya movio en ese turno o si no es un soldado del jugador actual, no puede mover. De lo contrario
     * llama a validMovePoints en board.
     * @param p Punto en el que se encuentra el soldado a mover.
     * @param windowOwner Dueño de la ventana que intenta mover.
     * @return El HashMap que reotrna validMovePoints en board.
     */
    public HashMap<Point, Boolean> validMovePoints(Point p, Player windowOwner) {
        Soldier s = board.getSoldier(p);
        if(s == null || windowOwner != currentPlayer || !s.canMove() || s.getOwner() != currentPlayer) {
            return new HashMap<>();
        }
        return board.validMovePoints(p);
    }


    /**
     * Mueve a un soldado. Si no movio en ese turno lo mueve en el tablero, deshabilita su movimiento, y llama
     * a performAction. Si ya movio en este turno no hace nada.
     * @param origin Punto en el que se encuentra el soldado.
     * @param dest Punto al que se mueve el soldado.
     */
    public void moveSoldier(Point origin, Point dest) {
        Soldier s = board.getSoldier(origin);
        if(s.canMove()) {
            board.moveSoldier(origin, dest);
            s.disableMovement();
            performAction();
            registerAction(new PendingDrawing(origin, dest, getSoldier(origin), ActionType.MOVEMENT));
        }
    }


    /**
     * Toma una carta del mazo y la agrega a la mano. Comprueba que el que intenta sacar la carta sea
     * el jugador actual, y llama a performAction si puedo sacar una carta.
     * @param player Jugador que tomara la carta.
     * @return Carta que se agrego a la mano.
     */
    public Card flipCard(Player player) {
        Card c = null;
        if(player == currentPlayer) {
            c = currentPlayer.cardToHand();
            if (c != null)
                performAction();
        }
        return c;
    }


    /**
     * Juega una carta magica o invoca a un soldado en un determinado punto.
     * @param c Carta a jugar.
     * @param p Punto en la cual se la invoca.
     */
    public void playCard(Card c, Point p) {
        if( c instanceof Soldier ) {
            board.addSoldier((Soldier) c, p);
            currentPlayer.playSoldier((Soldier) c);
            registerAction(new PendingDrawing(null, p, c, ActionType.MOVEMENT));
        } else if( c instanceof Magic ) {
            registerAction(new PendingDrawing(p, null, c, ActionType.CASTSPELL));
            ArrayList<Soldier> affectedBySpell = board.affectedBySpell(p);

            applyMagicToSoldiers(currentPlayer.getAliveCards());

            for(Soldier s: affectedBySpell) {
                if(!(s instanceof Hero)) {
                    s.curse((Magic) c);
                    registerAction(new PendingDrawing(null, board.searchSoldier(s), c, ActionType.RECEIVESPELL));
                    if(!s.isAlive())
                        removeDead(s);
                }
            }

        }
        currentPlayer.discardCard(c);
        performAction();
    }


    /**
     * Vuelve a permitir el movimiento en los soldados.
     * @param soldiers Arreglo de soldados a los cuales se les activara la posibilidad de moverse.
     */
    private void enableMovement(ArrayList<Soldier> soldiers) {
        for(Soldier s: soldiers)
            s.enableMovement();
    }


    /**
     * Primero realiza los ataques el jugador actual, luego aplica la magia a los soldados del juegador actual y
     * les activa la posibilidad de moverse. A continuacion realizan sus ataques los soldados del otro jugador.
     * Ademas, comprueba si la partida finalizo, es decir, si se destruyo el castillo o si el jugador no puede jugar.
     * Por ultimo, cambia el jugador actual y restaura las acciones restantes.
     */
    public void endTurn() {
        performAttack(currentPlayer.getAliveCards());
        applyMagicToSoldiers(currentPlayer.getAliveCards());
        enableMovement(currentPlayer.getAliveCards());

        Player otherPlayer = currentPlayer == player1 ? player2 : player1;
        performAttack(otherPlayer.getAliveCards());

        if(otherPlayer.getCastle().getLife() <= 0 || !otherPlayer.canPlay()) {
            otherPlayer.registerAction( new PendingDrawing(null, null, null, ActionType.LOSE) );
            currentPlayer.registerAction( new PendingDrawing(null, null, null, ActionType.WIN) );
        }
        if(currentPlayer.getCastle().getLife() <= 0 || !currentPlayer.canPlay()) {
            otherPlayer.registerAction( new PendingDrawing(null, null, null, ActionType.WIN) );
            currentPlayer.registerAction( new PendingDrawing(null, null, null, ActionType.LOSE) );
        }


        currentPlayer.registerAction(new PendingDrawing(null, null, null, ActionType.ENDTURN));

        currentPlayer = otherPlayer;

        currentPlayer.registerAction(new PendingDrawing(null, null, null, ActionType.STARTTURN));
        actionsLeft = 5;
    }


    public void writeGame(File file) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(this);
        oos.close();
    }


   public void loadGame(File file) throws IOException, ClassNotFoundException {
       ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
       Game saveGame = (Game) ois.readObject();
       this.currentPlayer = saveGame.currentPlayer;
       this.player1 = saveGame.player1;
       this.player2 = saveGame.player2;
       this.board = saveGame.board;
       this.actionsLeft = saveGame.actionsLeft;
       ois.close();
       currentPlayer.registerAction(new PendingDrawing(null, null, null, ActionType.STARTTURN));
   }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(player1);
        out.writeObject(player2);
        out.writeObject(currentPlayer);
        out.writeInt(actionsLeft);
        out.writeObject(board);
    }


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        player1 = (Player) ois.readObject();
        player2 = (Player) ois.readObject();
        currentPlayer = (Player) ois.readObject();
        actionsLeft = ois.readInt();
        board = (Board) ois.readObject();
    }

}
