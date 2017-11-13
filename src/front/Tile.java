package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

import static front.Board.CELLSIZE;

public class Tile {
    private static final Image corpseSprite = new Image("/graphics/soldiers/corpse.png");

    private int row;
    private int col;
    private TileStates status = TileStates.INACTIVE;
    private GraphicSoldier whosHere = null;
    private int corpseCount = 0;

    Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    Point getPos() {
        return new Point(row, col);
    }

    void changeStatus(TileStates status) {
        this.status = status;
    }

    TileStates getStatus() {
        return status;
    }

    public void setWhosHere(GraphicSoldier soldier) {
        whosHere = soldier;
    }

    public GraphicSoldier getWhosHere() {
        return whosHere;
    }

    public void addCorpse() {
        this.corpseCount++;
    }

    public ArrayList<Point> getAvailableMovements() {

        //back.Game.canMove(getPos());
        ArrayList<Point> list = new ArrayList<>();
        list.add(new Point(row - 1, col));
        list.add(new Point(row - 1, col - 1));
        list.add(new Point(row, col - 1));
        list.add(new Point(row + 1, col));
        list.add(new Point(row + 1, col + 1));
        list.add(new Point(row, col + 1));
        list.add(new Point(row - 1, col + 1));
        list.add(new Point(row + 1, col - 1));
        return list;
    }

    void moveSoldier(Point dir) {
        whosHere.move(dir);
    }

    void draw(GraphicsContext backgroundGC, GraphicsContext charGC, back.Player player1) {
        Color color = Color.TRANSPARENT;
        switch (status) {
            case INACTIVE: color = Color.TRANSPARENT; break;
            case ACTIVE: color = Color.rgb(0, 170, 41, 0.502); break;
            case MOVABLE: case INVOKABLE: color = Color.rgb(0, 17, 170, 0.502); break;
            case ATTACKABLE: color = Color.rgb(170, 30, 27, 0.65); break;
        }

        //backgroundGC.drawImage(new Image("graphics/map/pasto.png"), col*CELLSIZE, row*CELLSIZE);

        for(int i = 0; i < corpseCount; i++)
            backgroundGC.drawImage(new Image("graphics/soldiers/corpse.png"), col*CELLSIZE + 10 * i, row*CELLSIZE + 10 * i);

        backgroundGC.setFill(color);
        backgroundGC.setStroke(Color.rgb(0,0,0,0.3));
        backgroundGC.setLineWidth(2);
        backgroundGC.fillRect(col*CELLSIZE, row*CELLSIZE, CELLSIZE, CELLSIZE);
        backgroundGC.strokeRect(col*CELLSIZE, row*CELLSIZE, CELLSIZE, CELLSIZE);



        if (whosHere != null) {
            whosHere.drawMyself(getPos(), charGC, player1);
        }
    }
}
