package front;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GraphicCard {
    private back.Card card;
    private Canvas cardCanvas;

    GraphicCard(back.Card card) {
        this.card = card;
        this.cardCanvas = new Canvas(100, 100);

        GraphicsContext gc = cardCanvas.getGraphicsContext2D();

    }
}
