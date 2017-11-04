package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player extends ArrayList<Monster> {
    private Stack<Card> deck;
    private ArrayList<Card> hand;
    protected ArrayList<Monster> aliveCards;
    protected Castle castle;
    private int playerNumber;

    public Player(ArrayList<Card> cards, int castleLife, int playerNumber) {
        deck.addAll(cards);
        castle = new Castle(castleLife);
        this.playerNumber = playerNumber;
    }

    private boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    /* Automaticamente levanta una carta del mazo cuando juega, despues lo cambiamos */
    public boolean playCard(Monster m) {
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
