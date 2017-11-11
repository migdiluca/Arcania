package front;

import back.Soldier;
import com.sun.javafx.image.BytePixelSetter;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
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

    private back.Game game;
    private back.Player owner;

    private VBox createMenu() {
        VBox v = new VBox();

        Background vBackground = new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY));
        v.backgroundProperty().setValue(vBackground);

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

        /*Background hBackground = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
        h.backgroundProperty().setValue(hBackground);*/

        h.setMaxSize(400, 500);
        h.setMinSize(400, 500);


        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.2);

        ArrayList<ImageView> mano = new ArrayList<>();
        GraphicsContext gc;

        ArrayList<back.Card> cardsInHand = owner.getHand();

        int count = 0;
        for(back.Card c: cardsInHand) {
            ImageView elem = new ImageView();
            elem.setFitHeight(100);
            elem.setFitWidth(100);
            elem.setImage(new Image("carta.png"));
            h.getChildren().add(elem);
            mano.add(elem);

            elem.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    ImageView e = (ImageView) event.getSource();

                    titleHelp.setText("Caballero Oscuro");
                    infoHelp.setText("Guerrero de descomunal fuerza física, ideal en el combate rápido pero de olvidable destreza.");

                    for (ImageView carta : mano) {
                        carta.setEffect(null);
                    }
                    e.setEffect(colorAdjust);

                }
            });

            count++;
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

    Board(back.Game game, back.Player owner) {
        this.game = game;

        this.owner = owner;

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


        ImageView im = new ImageView(new Image("/graphics/ui/sword.png", 100, 100, true, false));

        im.setX(80);
        im.setY(90);

        pBoard.getChildren().addAll(backgroundCanvas, charCanvas, im);

        KeyFrame startFadeOut = new KeyFrame(Duration.seconds(0), new KeyValue(im.opacityProperty(), 1.0));
        KeyFrame endFadeOut = new KeyFrame(Duration.seconds(5), new KeyValue(im.opacityProperty(), 0.0));
        Timeline timelineOn = new Timeline(startFadeOut, endFadeOut);

        timelineOn.playFromStart();





        //addSoldier

        for(int i = 0; i < NUMROWS; i++)
            for(int j = 0; j < NUMCOLS; j++) {
            back.Soldier s = game.getBoard().getSoldier(new Point(i, j));
                if(s != null)
                    tiles[i][j].setWhosHere(new GraphicSoldier(s, owner.equals(s.getOwner())));

            }



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
                    /*tile.setWhosHere(auxTile.getWhosHere());
                    tile.moveSoldier(new Point(point.x - auxTile.getPos().x, point.y - auxTile.getPos().y));
*/
                    game.getBoard().moveSoldier(auxTile.getPos(), point);

                    auxTile = null;

                    game.endTurn();

                } else {
                    auxTile = null;
                    HashMap<Point, Boolean> moveAux = game.getBoard().validMovePoints(point, owner);

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
                tiles[i][j].draw(backgroundGC, charGC, game.getPlayer1());
            }
        }
    }

    private int fps = 0;
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (fps == 0 || fps == 5) {
                back.pendingDrawing action;

                while((action = owner.getActionRegistry()) != null) {
                    switch(action.getType()) {
                        case 0: //movimiento
                            if(action.getOrigin() == null) {//invocar
                                Tile dest = tiles[action.getDestination().x][action.getDestination().y];
                                dest.setWhosHere(new GraphicSoldier((back.Soldier) action.getCard(), action.getCard().getOwner().equals(owner)));

                            } else if(action.getDestination() == null) { //morir
                                Tile origin = tiles[action.getOrigin().x][action.getOrigin().y];
                                origin.setWhosHere(null);

                            } else { //mover
                                Tile origin = tiles[action.getOrigin().x][action.getOrigin().y];
                                Tile dest = tiles[action.getDestination().x][action.getDestination().y];

                                dest.setWhosHere( origin.getWhosHere() );

                                dest.moveSoldier(new Point(dest.getPos().x - origin.getPos().x, dest.getPos().y - origin.getPos().y));

                                origin.setWhosHere(null);
                            }
                            break;
                    }
                }

                draw();

                fps = 0;

            }
            fps++;

        }
    };
}
