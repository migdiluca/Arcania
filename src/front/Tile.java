package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;

import static front.Board.CELLSIZE;

/**
 * Clase que define un tile en el tablero
 */
class Tile {
    private static final Image corpseSprite = new Image("/graphics/soldiers/corpse.png");

    private int row;
    private int col;
    private TileStates status = TileStates.INACTIVE;
    private GraphicSoldier whosHere = null;
    private GraphicSpell spell = null;
    private int corpseCount = 0;
    private TileEffect effect;


    Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setEffect(TileEffect e) {
        this.effect = e;
    }

    /**
     *
     * @return par (x, y) que indican las coordenadas del Tile en el tablero
     */
    Point getPos() {
        return new Point(row, col);
    }

    void changeStatus(TileStates status) {
        this.status = status;
    }

    TileStates getStatus() {
        return status;
    }

    void setMagic(back.Magic m) {
        this.spell = new GraphicSpell(m);
    }

    void setWhosHere(GraphicSoldier soldier) {
        whosHere = soldier;
    }

    GraphicSoldier getWhosHere() {
        return whosHere;
    }

    /**
     * Aumenta la cantidad de cadáveres que deben dibujarse en el Tile
     */
    void addCorpse() {
        this.corpseCount++;
    }

    /**
     * Indica al GraphicSoldier que se ubica en el tile hacia donde moverse
     * @param dir
     */
    void moveSoldier(Point dir) {
        whosHere.move(dir);
    }

    /**
     * Dibuja el tile
     * @param backgroundGC GraphicContext correspondiente al canvas en donde se dibujarán las líneas separadoras de tiles y los cadáveres.
     * @param charGC GraphicContext correspondiente al canvas donde se dibujan el GraphicSoldier, el TileEffect, y el GraphicSpell
     * @param player1 instancia del jugador 1 en el back. Requerido para el correcto dibujado del soldado en la dirección que corresponda.
     */
    void draw(GraphicsContext backgroundGC, GraphicsContext charGC, back.Player player1) {
        Color color = Color.TRANSPARENT;
        switch (status) {
            case INACTIVE: color = Color.TRANSPARENT; break;
            case ACTIVE: color = Color.rgb(0, 170, 41, 0.4); break;
            case MOVABLE: case INVOKABLE: color = Color.rgb(0, 17, 170, 0.3); break;
            case ATTACKABLE: color = Color.rgb(170, 30, 27, 0.4); break;
        }

        for(int i = 0; i < corpseCount; i++)
            backgroundGC.drawImage(corpseSprite, col*CELLSIZE + 10 * i, row*CELLSIZE + 10 * i);

        backgroundGC.setFill(color);
        backgroundGC.setStroke(Color.rgb(0,0,0,0.3));
        backgroundGC.setLineWidth(2);
        backgroundGC.fillRect(col*CELLSIZE, row*CELLSIZE, CELLSIZE, CELLSIZE);
        backgroundGC.strokeRect(col*CELLSIZE, row*CELLSIZE, CELLSIZE, CELLSIZE);



        if (whosHere != null)
            whosHere.drawMyself(getPos(), charGC, player1);

        if (spell != null)
            if(spell.draw(getPos(), charGC)) spell = null;

        if(effect != null)
            if(effect.draw(getPos(), charGC)) effect = null;


    }
}
