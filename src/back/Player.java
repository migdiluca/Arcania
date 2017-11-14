package back;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private String name;
    private Stack<Card> deck ;
    protected ArrayList<Card> hand;
    protected ArrayList<Soldier> aliveCards;
    protected Castle castle;
    private int castleRow;
    private int actionsLeft;
    private static final long serialVersionUID = 1L;

    private ArrayDeque<pendingDrawing> actionRegistry;

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
            if( s instanceof Hero ) return (Hero) s;

        return null;
    }

    public void registerAction(pendingDrawing pd) {
        actionRegistry.add(pd);
    }

    public pendingDrawing getActionRegistry() {
        return actionRegistry.poll();
    }

    private boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public Card cardsToHand() {
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

    /* Automaticamente levanta una carta del mazo cuando juega, despues lo cambiamos */
    public void playSoldier(Soldier m) {
            aliveCards.add(m);
    }

    public void discardCard(Card c) {
        hand.remove(c);
    }

    public boolean canPlay() {
        if(aliveCards.isEmpty() && hand.isEmpty() && deck.isEmpty())
            return false;
        return true;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
        out.writeObject(deck);
        out.writeObject(hand);
        out.writeObject(aliveCards);
        out.writeObject(castle);
        out.writeObject(castleRow);
    }

    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        ois.defaultReadObject();
        name = (String) ois.readObject();
        deck = (Stack<Card>) ois.readObject();
        hand = (ArrayList<Card>) ois.readObject();
        aliveCards = (ArrayList<Soldier>) ois.readObject();
        castle = (Castle) ois.readObject();
        castleRow = (int) ois.readObject();
    }
}
