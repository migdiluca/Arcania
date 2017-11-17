package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class Player implements Serializable {
    private String name;
    private Stack<Card> deck ;
    private ArrayList<Card> hand;
    private ArrayList<Soldier> aliveCards;
    private Castle castle;
    private int castleRow;
    private static final long serialVersionUID = 1L;

    private ArrayDeque<pendingDrawing> actionRegistry;


    /** Crea un nuevo mazo, el arrayList en el que se encontraran las cartas ubicadas en la mano,
     * el ArrayList con las cartas vivas y el castillo del jugador. Le asgina la fila en la que se
     * encuentra la entrada del castillo. Para todas las cartas que se le asignan a player se les
     * establece player como su dueño. Se le asigna el nombre a player y se agrega las cartas al mazo.
     *
     * @param name Nombre del jugador.
     * @param cards Cartas que iran al mazo.
     * @param castleRow Fila en la que se ubica la entrada del castillo.
     */
    public Player(String name, ArrayList<Card> cards, int castleRow) {
        deck = new Stack<>();
        hand = new ArrayList<>();
        aliveCards = new ArrayList<>();
        castle = new Castle();
        this.castleRow = castleRow;

        for(Card s: cards)
            s.setOwner(this);

        this.name = name;
        deck.addAll(cards);

        actionRegistry = new ArrayDeque<>();
    }

    public Hero getHero() {
        for(Soldier s: aliveCards)
            if(s instanceof Hero)
                return (Hero) s;

        return null;
    }

    public Castle getCastle() {
        return castle;
    }

    public ArrayList<Soldier> getAliveCards() {
        return aliveCards;
    }

    public void registerAction(pendingDrawing pd) {
        actionRegistry.add(pd);
    }

    public pendingDrawing getActionRegistry() {
        return actionRegistry.poll();
    }


    /**
     * Toma una carta del mazo, la pasa a la mano y la retorna, si no hay mas cartas retorna nulo.
     * @return Carta que se paso a la mano.
     */
    public Card cardToHand() {
        Card c = null;
        if(!deck.empty()) {
            c = deck.pop();
            hand.add(c);
        }
        return c;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public String getName() { return name; }

    public int getCastleRow() {
        return castleRow;
    }

    /**
     * Agrega un jugador a las cartas vivas.
     * @param s Soldado a agregar.
     */
    public void playSoldier(Soldier s) {
        aliveCards.add(s);
    }

    public void discardCard(Card c) {
        hand.remove(c);
    }

    /**
     * Se fija si el jugador tiene jugadores para poder continuar el juego.
     * @return True si puede jugar, false si no puede.
     */
    public boolean canPlay() {
        for(Card c : hand) {
            if(c instanceof Soldier)
                return true;
        }
        for(Card c : deck) {
            if(c instanceof Soldier)
                return true;
        }
        if(!aliveCards.isEmpty())
            return true;
        return false;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(name);
        out.writeObject(deck);
        out.writeObject(hand);
        out.writeObject(aliveCards);
        out.writeObject(castle);
        out.writeInt(castleRow);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        name = ois.readUTF();
        deck = (Stack<Card>) ois.readObject();
        hand = (ArrayList<Card>) ois.readObject();
        aliveCards = (ArrayList<Soldier>) ois.readObject();
        castle = (Castle) ois.readObject();
        castleRow = ois.readInt();
    }
}
