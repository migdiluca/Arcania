package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static front.Board.CELLSIZE;

import java.awt.*;

public class TileEffect {

    private int framesCount;
    private int framesTotal;
    private int colorRed;
    private int colorGreen;
    private int colorBlue;

    TileEffect(int framesCount, int colorRed, int colorGreen, int colorBlue) {
        this.framesCount = this.framesTotal = framesCount;
        this.colorRed = colorRed;
        this.colorGreen = colorGreen;
        this.colorBlue = colorBlue;
    }

    public boolean draw(Point p, GraphicsContext gc) {
        framesCount--;

        gc.setFill(Color.rgb(colorRed, colorGreen, colorBlue, (double)framesCount / framesTotal));

        gc.fillRect(p.y * CELLSIZE, p.x * CELLSIZE, CELLSIZE, CELLSIZE);

        return(framesCount == 0);
    }
}
