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


    public void playSoldier(Soldier s) {
        aliveCards.add(s);
    }

    public void discardCard(Card c) {
        hand.remove(c);
    }

    public boolean canPlay() {
        if(aliveCards.isEmpty() && hand.isEmpty() && deck.isEmpty())
            return false;
        return true;
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
