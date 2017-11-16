package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;

import static front.Board.CELLSIZE;

/**
 * Clase encargada de definir y dibujar, durante el período establecido, un efecto gráfico sobre un Tile.
 */

class TileEffect {

    private int framesCount;
    private int framesTotal;
    private int colorRed;
    private int colorGreen;
    private int colorBlue;

    /**
     * Constructor.
     * @param framesCount cantidad de frames que debe durar la animación antes de desaparecer.
     * @param colorRed entero que define la cantidad de rojo que se usará
     * @param colorGreen entero que define la cantidad de verde que se usará
     * @param colorBlue entero que define la cantidad de azul que se usará
     */
    TileEffect(int framesCount, int colorRed, int colorGreen, int colorBlue) {
        this.framesCount = this.framesTotal = framesCount;
        this.colorRed = colorRed;
        this.colorGreen = colorGreen;
        this.colorBlue = colorBlue;
    }

    /**
     * Dibuja el efecto en el Tile correspondiente y con una opacidad dada por la relación entre las variables de instancia framesCount y framesTotal
     * @param p punto que representa el Tile sobre el que se debe dibujar
     * @param gc GraphicContext del canvas sobre el que se dibuja (charGC)
     * @return falso si la animación no terminó. Verdadero cuando termina.
     */

    boolean draw(Point p, GraphicsContext gc) {
        framesCount--;

        gc.setFill(Color.rgb(colorRed, colorGreen, colorBlue, (double)framesCount / framesTotal));

        gc.fillRect(p.y * CELLSIZE, p.x * CELLSIZE, CELLSIZE, CELLSIZE);

        return(framesCount == 0);
    }
}
