package front;

import back.MagicDamage;
import back.Poison;
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
    private static final int FRAMETOTAL = 15;
    private static final int FRAMEWIDTH = 100;
    private static final int FRAMEHEIGHT = 100;

    GraphicSpell(back.Magic m) {
        if (m instanceof back.Inspiration)
            sprite = new Image("graphics/spells/505.png");
        else if (m instanceof back.Poison)
            sprite = new Image("graphics/spells/502.png");
        else if (m instanceof back.MagicDamage)
            sprite = new Image("graphics/spells/507.png");
        else if (m instanceof back.Heal)
            sprite = new Image("graphics/spells/501.png");
    }

    /**
     * Dibuja el frame nºframeCount del sprite
     * @param p punto que refiere al Tile sobre el que se debe dibujar
     * @param gc GraphicContext del canvas sobre el que se dibuja (charGC)
     * @return falso si la animación no terminó. Verdadero caso contrario.
     */
    public boolean draw(Point p, GraphicsContext gc) {

        gc.drawImage(sprite, FRAMEWIDTH * frameCount, 0, FRAMEWIDTH, FRAMEHEIGHT, p.y * CELLSIZE,p.x * CELLSIZE, FRAMEWIDTH, FRAMEHEIGHT);

        frameCount++;

        return(frameCount == FRAMETOTAL);
    }

}
