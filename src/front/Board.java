package front;

import com.sun.javafx.image.BytePixelSetter;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.omg.PortableInterceptor.INACTIVE;
import sun.font.GraphicComponent;

import static front.Tile.INACTIVE;
import static front.Tile.ACTIVE;
import static front.Tile.SELECTABLE;
import static front.Tile.ATTACKABLE;

public class Board extends HBox {
    private static final int NUMROWS = 7;
    private static final int NUMCOLS = 7;
    static final int CELLHEIGHT = 100;
    static final int CELLWIDTH = 100;

    private HashSet<GraphicSoldier> gSoldiers = new HashSet<>();
    private Tile[][] tiles = new Tile[NUMROWS][NUMCOLS];

    private Canvas backgroundCanvas;
    private GraphicsContext backgroundGC;
    private Canvas charCanvas;
    private GraphicsContext charGC;

    private Pane pBoard;
    private Tile auxTile = null;

    public back.Board b;

    private VBox createMenu() {
        VBox v = new VBox();

        v.setPadding(new Insets(10));


        GridPane info = new GridPane();
        Text titleHelp = new Text("Información de selección");
        titleHelp.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        info.add(titleHelp, 1, 1);

        Label infoHelp = new Label("Seleccione una carta para ver información de la misma.");
        infoHelp.setWrapText(true);
        info.add(infoHelp, 1, 2);


        Text title = new Text("Mano");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        v.getChildren().add(title);

        FlowPane h = new FlowPane();

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.2);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        ImageView mano[] = new ImageView[30];
        Canvas hand[] = new Canvas[30];
        GraphicsContext gc;

        for (int i = 0; i < 30; i++) {

            hand[i] = new Canvas(168,155);

            gc = hand[i].getGraphicsContext2D();
            gc.drawImage(new Image("graphics/ui/MARCO.png"), 0,0);
            gc.drawImage(new Image("graphics/ui/BANDERA.png"), 18,0);

            /*mano[i] = new ImageView();
            mano[i].setFitHeight(100);
            mano[i].setFitWidth(100);
            mano[i].setImage(new Image("carta.png"));
            h.getChildren().add(mano[i]);*/
            h.getChildren().add(hand[i]);

            hand[i].setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    //ImageView e = (ImageView) event.getSource();

                    titleHelp.setText("Caballero Oscuro");
                    infoHelp.setText("Guerrero de descomunal fuerza física, ideal en el combate rápido pero de olvidable destreza.");

                    /*for (ImageView carta : mano) {
                        carta.setEffect(null);
                    }
                    e.setEffect(colorAdjust);*/

                }
            });


        }

        ScrollPane scrollPane = new ScrollPane(h);
        scrollPane.setPrefSize(450, 450);
        scrollPane.setContent(h);
        scrollPane.setPadding(new Insets(5));

        v.getChildren().add(scrollPane);



        v.getChildren().add(info);



        /*Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};

        for (int i=0; i<4; i++) {
            GridPane.setMargin(options[i], new Insets(0, 0, 0, 8));
            v.add(options[i], i, 2);
        }*/



        return v;
    }

    Board() {


        b = new back.Board();


        backgroundCanvas = new Canvas(NUMCOLS * CELLWIDTH, NUMROWS * CELLHEIGHT);
        backgroundGC = backgroundCanvas.getGraphicsContext2D();
        charCanvas = new Canvas(NUMCOLS * CELLWIDTH, NUMROWS * CELLHEIGHT);
        charGC = charCanvas.getGraphicsContext2D();

        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                tiles[i][j] = new Tile(i, j);
            }
        }

        pBoard = new Pane();
        pBoard.setMaxSize(700, 700);
        pBoard.setMinSize(700, 700);

        pBoard.getChildren().addAll(backgroundCanvas, charCanvas);



        //addMonster
        back.Heroe m1 = new back.Heroe("Fausto", 100, 100, 25, 4);
        back.Monster m2 = new back.Monster("Caballero Negro", 150, 80, 25, 4);

        b.addMonster(m1, new Point(2,2));
        tiles[2][2].setWhosHere(new GraphicSoldier(m1, false));
        b.addMonster(m2, new Point(5,5));
        tiles[5][5].setWhosHere(new GraphicSoldier(m2, true));

        getChildren().addAll(pBoard, createMenu());
        //setStyle("-fx-background-color: #5490ff");
        charCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point point = getPointFromCoordinates((int) event.getX(), (int) event.getY());
                Tile tile = tiles[point.x][point.y];
                int status = tile.getStatus();

                for (int i = 0; i < NUMROWS; i++) {
                    for (int j = 0; j < NUMCOLS; j++) {
                        tiles[i][j].changeStatus(INACTIVE);
                    }
                }


                if (status == SELECTABLE || status == ATTACKABLE) {
                    //back.Game.moveSoldier(auxTile.getPos(), point);
                    tile.setWhosHere(auxTile.getWhosHere());
                    tile.moveSoldier(new Point(point.x - auxTile.getPos().x, point.y - auxTile.getPos().y));

                    b.moveMonster(auxTile.getPos(), point);

                    auxTile.setWhosHere(null);
                    auxTile = null;
                } else {
                    auxTile = null;
                    HashMap<Point, Boolean> moveAux = b.validMovePoints(point);

                    if (status != ACTIVE && !moveAux.isEmpty()) {
                        for (Point p: moveAux.keySet()) {
                            if(moveAux.get(p) == Boolean.TRUE)
                                tiles[p.x][p.y].changeStatus(ATTACKABLE);
                            else
                                tiles[p.x][p.y].changeStatus(SELECTABLE);
                        }
                        tile.changeStatus(ACTIVE);
                        auxTile = tile;
                    }
                }
            }
        });
        timer.start();
    }

    private Point getPointFromCoordinates(int x, int y) {
        int i = x / CELLWIDTH;
        int j = y / CELLHEIGHT;
        if(i < 0 || i > NUMROWS * CELLWIDTH || j < 0 || j > NUMCOLS * CELLWIDTH) {
            throw new IllegalArgumentException();
        }
        return new Point(j, i);
    }

    private int frame = 0;

    private void draw() {
        backgroundGC.clearRect(0, 0, backgroundCanvas.getWidth(), backgroundCanvas.getHeight());
        charGC.clearRect(0, 0, charCanvas.getWidth(), charCanvas.getHeight());


        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                tiles[i][j].draw(backgroundGC, charGC);
            }
        }
    }

    private int fps = 0;
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (fps == 0 || fps == 3) {
                draw();
                fps = 0;
            }
            fps++;
        }
    };
}
