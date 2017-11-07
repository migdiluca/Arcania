package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player extends ArrayList<Soldier> {
    private Stack<Card> deck;
    private ArrayList<Card> hand;
    protected ArrayList<Soldier> aliveCards;
    protected Castle castle;
    private int castleRow;

    public Player(ArrayList<Card> cards, int castleLife, int castleRow) {
        deck.addAll(cards);
        castle = new Castle(castleLife);
        this.castleRow = castleRow;
    }

    private boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

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
