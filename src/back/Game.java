package back;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Game {
    private Player players[];
    private Board board;
    private Stack<Card> allCards;

    public Game() {
        players = new Player[2];
       /* players[0] = new Player();
        players[1] = new Player();*/
        board = new Board();
    }

    private void removeDead(Monster monster) {

    }

    public int endTurn() {
        //funcion de ataque
        if(players[0].castle.getLife() <= 0 || !players[0].canPlay())
            return 0;
        if(players[1].castle.getLife() <= 0 || !players[1].canPlay())
            return 1;
        return -1;
    }
}
