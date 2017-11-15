package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import static front.Board.CELLSIZE;

/**
 * Clase que contiene la información de una animación de hechizo que debe dibujarse sobre un tile
 */

public class GraphicSpell {
    private Image sprite;
    private int frameCount = 0;
    private static final int FRAMETOTAL = 6;
    private static final int FRAMEWIDTH = 100;
    private static final int FRAMEHEIGHT = 100;

    GraphicSpell(back.Magic m) {
        sprite = new Image("graphics/soldiers/1.png");
    }

    /**
     * Dibuja el frame nºframeCount del sprite
     * @param p punto que refiere al Tile sobre el que se debe dibujar
     * @param gc GraphicContext del canvas sobre el que se dibuja (charGC)
     * @return falso si la animación no terminó. Verdadero caso contrario.
     */
    public boolean draw(Point p, GraphicsContext gc) {
        frameCount++;

        gc.drawImage(sprite, FRAMEWIDTH * frameCount, 0, FRAMEWIDTH, FRAMEHEIGHT, p.y * CELLSIZE,p.x * CELLSIZE, FRAMEWIDTH, FRAMEHEIGHT);

        return(frameCount == FRAMETOTAL);
    }

}
