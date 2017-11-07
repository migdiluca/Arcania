package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

import static front.Board.CELLHEIGHT;
import static front.Board.CELLWIDTH;

public class Tile {
    static final int INACTIVE = 0;
    static final int ACTIVE = 1;
    static final int SELECTABLE = 2;
    static final int ATTACKABLE = 3;

    private int row;
    private int col;
    private int status;
    private GraphicSoldier whosHere;

    Tile(int row, int col) {
        this.status = INACTIVE;
        this.whosHere = null;
        this.row = row;
        this.col = col;
    }

    Point getPos() {
        return new Point(row, col);
    }

    void changeStatus(int status) {
        this.status = status;
    }

    int getStatus() {
        return status;
    }

    public void setWhosHere(GraphicSoldier soldier) {
        whosHere = soldier;
    }

    public GraphicSoldier getWhosHere() {
        return whosHere;
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

    void draw(GraphicsContext backgroundGC, GraphicsContext charGC) {
        Color color = Color.TRANSPARENT;
        switch (status) {
            case INACTIVE: color = Color.TRANSPARENT; break;
            case ACTIVE: color = Color.rgb(0, 170, 41, 0.502); break;
            case SELECTABLE: color = Color.rgb(0, 17, 170, 0.502); break;
            case ATTACKABLE: color = Color.rgb(170, 30, 27, 0.65); break;
        }

        backgroundGC.drawImage(new Image("graphics/map/pasto.png"), col*CELLWIDTH, row*CELLHEIGHT);

        backgroundGC.setFill(color);
        backgroundGC.setStroke(Color.BLACK);
        backgroundGC.setLineWidth(2);
        backgroundGC.fillRect(col*CELLWIDTH, row*CELLHEIGHT, CELLWIDTH, CELLHEIGHT);
        backgroundGC.strokeRect(col*CELLWIDTH, row*CELLHEIGHT, CELLWIDTH, CELLHEIGHT);

        if (whosHere != null) {
            whosHere.drawMyself(getPos(), charGC);
        }
    }
}
