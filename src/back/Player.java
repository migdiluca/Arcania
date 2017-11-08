package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player extends ArrayList<Soldier> {
    private String name;
    private Stack<Card> deck;
    private ArrayList<Card> hand;
    protected ArrayList<Soldier> aliveCards;
    protected Castle castle;
    private int castleRow;

    public Player(String name, ArrayList<Card> cards, int castleRow) {
        for(Card s: cards)
            s.setOwner(this);
        this.name = name;
        deck.addAll(cards);
        castle = new Castle();
        this.castleRow = castleRow;
    }

    private boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public Soldier getPlayer() {
        return (Soldier) deck.pop();
    }

    public String getName() { return name; }

    public int getCastleRow() {
        return castleRow;
    }

    /* Automaticamente levanta una carta del mazo cuando juega, despues lo cambiamos */
    public boolean playCard(Soldier m) {
        if(hand.remove(m)){
            aliveCards.add(m);
            takeCard();
            return true;
        }
        return false;
    }

    public boolean canPlay() {
        if(aliveCards.isEmpty() && hand.isEmpty() && deck.isEmpty())
            return false;
        return true;
    }
}
