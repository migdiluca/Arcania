package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private Stack<Card> deck;
    private ArrayList<Card> hand;
    private Castle castle;

    public Player(ArrayList<Cards> cards, int castleLife) {
        for(Card s : cards) {
            s.assignPlayer(this);
        }
        deck.addAll(cards);
        castle = new Castle(castleLife);
    }

    public boolean takeCard() {
        if(deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public Castle getCastle() {
        return castle;
    }
}
