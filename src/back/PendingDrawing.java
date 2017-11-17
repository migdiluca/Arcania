package back;

import java.awt.*;
import java.io.Serializable;

/**
 * Clase que representa un elemento pendiente de dibujado que posteriormente el cliente levantará.
 */
public class PendingDrawing implements Serializable {
    private Point origin;
    private Point destination;
    private Card card;

    private ActionType actionType;

    PendingDrawing(Point origin, Point destination, Card card, ActionType actionType) {
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
