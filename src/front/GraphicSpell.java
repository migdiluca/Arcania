package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import static front.Board.CELLSIZE;

public class GraphicSpell {
    private Image sprite;
    private int frameCount = 0;
    private static final int FRAMETOTAL = 6;
    private static final int FRAMEWIDTH = 100;
    private static final int FRAMEHEIGHT = 100;

    GraphicSpell(back.Magic m) {
        sprite = new Image("graphics/soldiers/1.png");
    }

    public boolean draw(Point p, GraphicsContext gc) {
        frameCount++;

        gc.drawImage(sprite, FRAMEWIDTH * frameCount, 0, FRAMEWIDTH, FRAMEHEIGHT, p.y * CELLSIZE,p.x * CELLSIZE, FRAMEWIDTH, FRAMEHEIGHT);

        return(frameCount == FRAMETOTAL);
    }

}
