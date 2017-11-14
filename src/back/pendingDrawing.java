package back;

import java.awt.*;

public class pendingDrawing {
    private Point origin;
    private Point destination;
    private Card card;

    private ActionType actionType; //0 mover, 1 atacar, 2 conjuro

    pendingDrawing(Point origin, Point destination, Card card, ActionType actionType) {
        this.origin = origin;
        this.destination = destination;
        this.card = card;
        this.actionType = actionType;
    }

    public Point getOrigin() {
        return origin;
    }

    public Point getDestination() {
        return destination;
    }

    public Card getCard() {
        return card;
    }

    public ActionType getType() {
        return actionType;
    }

}
