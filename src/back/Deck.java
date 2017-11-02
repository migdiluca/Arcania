package TPE;

import java.util.ArrayDeque;
import java.util.Deque;

public class Deck {

    private Deque<Card> deck;

    public Deck(){
        this.deck = new ArrayDeque<Card>(40);
    }
}
