package front;

import back.ActionType;
import back.Soldier;
import com.sun.javafx.image.BytePixelSetter;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.omg.PortableInterceptor.INACTIVE;
import sun.font.GraphicComponent;

public class Board extends Pane {
    private static final int NUMROWS = 7;
    private static final int NUMCOLS = 7;
    static final int CELLSIZE = 100;

    private Map<Canvas, back.Card> cardsInHand = new HashMap<>();
    private Canvas selectedCard = null;

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

    private Label infoHelp;
    private Text titleHelp;

    private FlowPane h;

    private Label movesLeft;

    private VBox createMenu() {
        VBox v = new VBox(20);

        /*Background vBackground = new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY));
        v.backgroundProperty().setValue(vBackground);*/

        v.setPadding(new Insets(10));

        GridPane info = new GridPane();
        titleHelp = new Text("Información de selección");
        titleHelp.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleHelp.setFill(Color.WHITE);
        info.add(titleHelp, 1, 1);

        infoHelp = new Label("Seleccione una carta para ver información de la misma.");
        infoHelp.setWrapText(true);
        infoHelp.setPrefWidth(300);
        //infoHelp.setPrefHeight(80);
        infoHelp.setTextFill(Color.grayRgb(180));
        info.add(infoHelp, 1, 2);


        //Text title = new Text("Mano");
        //title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        //v.getChildren().add(title);

        h = new FlowPane();

        /*Background hBackground = new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY));
        h.backgroundProperty().setValue(hBackground);*/

        h.setMaxSize(350, 1200);
        h.setMinSize(350, 1200);

        //Background bHand = new Background(new BackgroundImage(new Image("graphics/ui/hand.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));

        ArrayList<back.Card> cardsAux = owner.getHand();

        int count = 0;
        for(back.Card c: cardsAux)
            addCardToHand(c);


        ScrollPane scrollPane = new ScrollPane(h);
        scrollPane.setPrefSize(400,  590);
        scrollPane.setContent(h);
        scrollPane.setPadding(new Insets(80, 0, 30, 15));

        /*h.setBackground(bHand);
        scrollPane.setBackground(bHand);*/

        v.getChildren().add(scrollPane);

        v.getChildren().add(info);

        movesLeft = new Label("Movimientos restantes: 5");
        movesLeft.setTextFill(Color.WHITE);

        Button drawCardBtn = new Button("Sacar carta");
        drawCardBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                back.Card c = game.flipCard(owner);
                if( c != null) addCardToHand(c);

                updateActionsLeft();
            }
        });

        Button endTurnBtn = new Button("Finalizar turno");
        endTurnBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.endTurn();
            }
        });

        v.getChildren().addAll(movesLeft, drawCardBtn, endTurnBtn);





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

    private void addCardToHand(back.Card c) {
        Canvas cardCanvas = new Canvas(170,170);
        GraphicsContext cardGC = cardCanvas.getGraphicsContext2D();

        cardGC.drawImage(new Image("graphics/ui/MARCO.png", 160, 160, true, true), 10, 10);
        cardGC.drawImage(new Image("graphics/ui/BANDERA.png", 100, 100, true, true), 25, 10);

        cardGC.setFill(Color.WHITE);
        //cardGC.setFont(titleHelp.getFont());


        if( c instanceof back.Soldier ) {
            back.Soldier s = (back.Soldier) c;
            cardGC.fillText(String.valueOf(s.getAttack()), 53, 24);
            cardGC.fillText(String.valueOf(s.getHealth()), 53, 40);
            cardGC.fillText(String.valueOf(s.getAgility()), 53, 56);
            cardGC.fillText(String.valueOf(s.getDefense()), 53, 72);

        }

        cardGC.setFont(titleHelp.getFont());
        cardGC.fillText(c.getName(), 43, 145);

        h.getChildren().add(cardCanvas);

        cardsInHand.put(cardCanvas, c);

        cardCanvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Canvas e = (Canvas) event.getSource();
                if(e != selectedCard ) e.setEffect(new Glow(0.7));

                if(selectedCard != null) return;

                back.Card card = cardsInHand.get(e);

                titleHelp.setText(card.getName());
                infoHelp.setText(card.getDescription());

            }
        });

        cardCanvas.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Canvas e = (Canvas) event.getSource();

                if(e != selectedCard) e.setEffect(null);

                if(selectedCard != null) return;

                titleHelp.setText("Información de selección");
                infoHelp.setText("Seleccione una carta para ver información de la misma.");

            }
        });

        cardCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(game.getCurrentPlayer() != owner) return;

                resetTileStatus();

                Canvas e = (Canvas) event.getSource();
                if(e == selectedCard) {
                    e.setEffect(new Glow(0.7));
                    selectedCard = null;
                } else {
                    ArrayList<Point> moveAux = game.availableSpawns(cardsInHand.get(e));
                    for (Point p: moveAux) {
                        tiles[p.x][p.y].changeStatus(TileStates.INVOKABLE);
                    }

                    Glow glow = new Glow(0.7);

                    if(selectedCard != null) selectedCard.setEffect(null);

                    DropShadow borderGlow = new DropShadow();

                    back.Card card = cardsInHand.get(e);

                    titleHelp.setText(card.getName());
                    infoHelp.setText(card.getDescription());

                    borderGlow.setInput(glow);

                    borderGlow.setOffsetY(0f);
                    borderGlow.setOffsetX(0f);
                    borderGlow.setColor(Color.rgb(255, 222, 107, 0.5));

                    borderGlow.setWidth(30);
                    borderGlow.setHeight(30);

                    e.setEffect(borderGlow);

                    selectedCard = e;
                }


            }
        });

    }

    private void updateActionsLeft() {
        movesLeft.setText("Movimientos restantes: " + game.getActionsLeft());
    }

    private void resetTileStatus() {
        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                tiles[i][j].changeStatus(TileStates.INACTIVE);
            }
        }
    }

    Board(back.Game game, back.Player owner) {


        ImageView background = new ImageView(new Image("/graphics/map/fondo.png", 1300, 820, false, false));

        HBox hb = new HBox(80);

        hb.setLayoutX(78);
        hb.setLayoutY(75);

        getChildren().addAll(background, hb);


        this.game = game;

        this.owner = owner;

        backgroundCanvas = new Canvas(NUMCOLS * CELLSIZE, NUMROWS * CELLSIZE);
        backgroundGC = backgroundCanvas.getGraphicsContext2D();
        charCanvas = new Canvas(NUMCOLS * CELLSIZE, NUMROWS * CELLSIZE);
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
            back.Soldier s = game.getSoldier(new Point(i, j));
                if(s != null)
                    tiles[i][j].setWhosHere(new GraphicSoldier(s, owner.equals(s.getOwner())));

            }

        VBox menu = createMenu();
        menu.setTranslateY(-60);
        hb.getChildren().addAll(pBoard, menu);


        /*Música
        MediaPlayer mp = new MediaPlayer(new Media(getClass().getResource("sounds/bg-music.mp3").toString()));
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                mp.seek(Duration.ZERO);
            }
        });
        mp.play();*/


        //setStyle("-fx-background-color: #5490ff");
        charCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point point = getPointFromCoordinates((int) event.getX(), (int) event.getY());
                Tile tile = tiles[point.x][point.y];
                TileStates status = tile.getStatus();

                resetTileStatus();
                if(selectedCard != null) selectedCard.setEffect(null);


                if (status == TileStates.MOVABLE || status == TileStates.ATTACKABLE) {
                    //back.Game.moveSoldier(auxTile.getPos(), point);
                    /*tile.setWhosHere(auxTile.getWhosHere());
                    tile.moveSoldier(new Point(point.x - auxTile.getPos().x, point.y - auxTile.getPos().y));
*/
                    game.moveSoldier(auxTile.getPos(), point);

                    auxTile = null;

                    updateActionsLeft();

                } else if(status == TileStates.INVOKABLE) {
                    back.Card card = cardsInHand.get(selectedCard);
                    game.playCard(card, point);

                    h.getChildren().remove(selectedCard);

                    titleHelp.setText("Información de selección");
                    infoHelp.setText("Seleccione una carta para ver información de la misma.");

                    selectedCard = null;
                    auxTile = null;

                    updateActionsLeft();
                } else {
                    auxTile = null;
                    HashMap<Point, Boolean> moveAux = game.validMovePoints(point, owner);

                    if (status != TileStates.ACTIVE && !moveAux.isEmpty()) {

                        titleHelp.setText(tile.getWhosHere().getSoldier().getName());
                        infoHelp.setText(tile.getWhosHere().getSoldier().getDescription());

                        for (Point p: moveAux.keySet()) {
                            if(moveAux.get(p) == Boolean.TRUE)
                                tiles[p.x][p.y].changeStatus(TileStates.ATTACKABLE);
                            else
                                tiles[p.x][p.y].changeStatus(TileStates.MOVABLE);
                        }
                        tile.changeStatus(TileStates.ACTIVE);
                        auxTile = tile;
                    }
                }
            }
        });
        timer.start();
    }

    private Point getPointFromCoordinates(int x, int y) {
        int i = x / CELLSIZE;
        int j = y / CELLSIZE;
        if(i < 0 || i > NUMROWS * CELLSIZE || j < 0 || j > NUMCOLS * CELLSIZE) {
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
                        case MOVEMENT: //movimiento
                            if(action.getOrigin() == null) {//invocar
                                Tile dest = tiles[action.getDestination().x][action.getDestination().y];
                                dest.setWhosHere(new GraphicSoldier((back.Soldier) action.getCard(), action.getCard().getOwner().equals(owner)));

                            } else if(action.getDestination() == null) { //morir
                                Tile origin = tiles[action.getOrigin().x][action.getOrigin().y];
                                origin.addCorpse();
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
