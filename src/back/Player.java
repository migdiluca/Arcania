package back;

import java.util.ArrayList;
import java.util.Stack;

public class Player {
    private String name;
    private Stack<Card> deck ;
    protected ArrayList<Card> hand;
    protected ArrayList<Soldier> aliveCards;
    protected Castle castle;
    private int castleRow;

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
    }

    private boolean takeCard() {
        if (deck.empty())
            return false;
        hand.add(deck.pop());
        return true;
    }

    public void cardsToHand() {
        cardsToHand(1);
    }

    public void cardsToHand(int ammount) {
        int i = 0;
        while(i <= ammount) {
            if(!deck.empty())
                hand.add(deck.pop());
            else
                i = ammount;
            i++;
        }
    }

    public ArrayList<Card> getHand() {
        return hand;
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
