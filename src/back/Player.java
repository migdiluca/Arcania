package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private Stack<Card> deck;
    private ArrayList<Card> hand;
    protected ArrayList<Card> aliveCards;
    protected Castle castle;

    public Player(ArrayList<Card> cards, int castleLife) {
        deck.addAll(cards);
        castle = new Castle(castleLife);
    }

    public boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public boolean canPlay() {
        if(aliveCards.isEmpty() && hand.isEmpty() && deck.isEmpty())
            return false;
        return true;
    }
}
