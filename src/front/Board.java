package front;

import back.PendingDrawing;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.awt.*;
import java.util.*;

import static back.Castle.CASTLELIFE;

/**
 * Clase que representa la ventana de un jugador.
 */

class Board extends Pane {
    private static final int NUMROWS = 7;
    private static final int NUMCOLS = 7;
    static final int CELLSIZE = 100;

    private Map<Canvas, back.Card> cardsInHand = new HashMap<>();
    private Canvas selectedCard = null;

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

    private Button drawCardBtn;
    private Button endTurnBtn;

    private Timer timeLeft;

    private ProgressBar scrollTimeLeft;

    private ProgressBar castle1Indicator;
    private ProgressBar castle2Indicator;

    private Label lblAlerts;

    private MediaPlayer mp;
    private final AudioClip changeTurnSound = new AudioClip(ClassLoader.getSystemResource("sounds/endturn.wav").toString());
    private boolean silent;


    /**
     * Crea los controles de la parte derecha del formulario, (lo que no es el board mismo).
     * @return Instancia del panel VBox que contiene los elementos del menú de la derecha.
     */
    private VBox createMenu() {
        VBox v = new VBox(15);

        v.setPadding(new Insets(10));

        GridPane info = new GridPane();

        titleHelp = new Text("Información de selección");
        titleHelp.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleHelp.setFill(Color.WHITE);
        info.add(titleHelp, 1, 1);

        infoHelp = new Label("Seleccione una carta para ver información de la misma.");
        infoHelp.setWrapText(true);
        infoHelp.setPrefWidth(380);
        infoHelp.setPrefHeight(60);
        infoHelp.setAlignment(Pos.TOP_LEFT);
        infoHelp.setTextFill(Color.grayRgb(180));
        info.add(infoHelp, 1, 2);

        h = new FlowPane();
        h.setPrefWidth(350);

        ArrayList<back.Card> cardsAux = owner.getHand();

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


        movesLeft = new Label();
        movesLeft.setTextFill(Color.WHITE);
        updateActionsLeft();

        drawCardBtn = new Button("Sacar carta");
        drawCardBtn.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                back.Card c = game.flipCard(owner);
                if( c != null) addCardToHand(c);

                updateActionsLeft();
            }
        });

        endTurnBtn = new Button("Finalizar turno");
        endTurnBtn.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                game.endTurn();
            }
        });


        scrollTimeLeft = new ProgressBar();
        scrollTimeLeft.setProgress(0.9);
        scrollTimeLeft.setPrefSize(150, 15);
        scrollTimeLeft.setDisable(true);


        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(drawCardBtn, endTurnBtn);
        buttonBox.setAlignment(Pos.CENTER);

        HBox turnBox = new HBox(10);
        turnBox.getChildren().addAll(scrollTimeLeft, movesLeft);
        turnBox.setAlignment(Pos.CENTER);

        v.setAlignment(Pos.CENTER);

        v.getChildren().addAll(turnBox, buttonBox);

        return v;
    }

    /**
     * Método que genera el canvas que representa a una carta y la agrega a la mano.
     * @param c instancia del objeto Card del backend de la carta en cuestión
     */

    private void addCardToHand(back.Card c) {
        Canvas cardCanvas = new Canvas(170,170);
        GraphicsContext cardGC = cardCanvas.getGraphicsContext2D();


        cardGC.drawImage(new Image("graphics/avatars/" + c.getID() + ".png", 110, 110, true, true), 40, 30);

        if( c instanceof back.Magic ) {
            ColorAdjust ca = new ColorAdjust();
            if( ((back.Magic) c).getIsNegative() )
                ca.setHue(-0.2);
            else
                ca.setHue(0.2);
            cardGC.setEffect(ca);
        }
        cardGC.drawImage(new Image("graphics/ui/MARCO.png", 160, 160, true, true), 10, 10);
        cardGC.setEffect(null);


        cardGC.setFill(Color.WHITE);

        if( c instanceof back.Soldier ) {
            cardGC.drawImage(new Image("graphics/ui/banner.png", 115,115, true, false), 22, 10);
            back.Soldier s = (back.Soldier) c;
            cardGC.drawImage(new Image("graphics/ui/attack.png", 14,14, true, true), 36, 16);
            cardGC.fillText(String.valueOf(s.getAttack()), 58, 27);
            cardGC.drawImage(new Image("graphics/ui/life.png", 14,14, true, true), 36, 33);
            cardGC.fillText(String.valueOf(s.getHealth()), 58, 44);
            cardGC.drawImage(new Image("graphics/ui/agility.png", 14,14, true, true), 36, 50);
            cardGC.fillText(String.valueOf(s.getAgility()), 58, 61);
            cardGC.drawImage(new Image("graphics/ui/defense.png", 14,14, true, true), 36, 67);
            cardGC.fillText(String.valueOf(s.getDefense()), 58, 78);
        }




        cardGC.setFont(new Font(12));
        cardGC.setTextAlign(TextAlignment.CENTER);
        cardGC.fillText(c.getName(), 90, 147);

        h.getChildren().add(cardCanvas);

        cardsInHand.put(cardCanvas, c);

        cardCanvas.setOnMouseEntered(new EventHandler<>() {
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

        cardCanvas.setOnMouseExited(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                Canvas e = (Canvas) event.getSource();

                if(e != selectedCard) e.setEffect(null);

                if(selectedCard != null) return;

                titleHelp.setText("Información de selección");
                infoHelp.setText("Seleccione una carta para ver información de la misma.");

            }
        });

        cardCanvas.setOnMouseClicked(new EventHandler<>() {
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

    /**
     * Actualiza el label de movimientos restantes
     */
    private void updateActionsLeft() {
        movesLeft.setText("Movimientos restantes: " + game.getActionsLeft());
    }

    /**
     * Reinicia el estado de todos los tiles a default
     */
    private void resetTileStatus() {
        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                tiles[i][j].changeStatus(TileStates.INACTIVE);
            }
        }
    }

    private void showAlertText(String text) {
        lblAlerts.setText(text);
        KeyFrame startFadeText = new KeyFrame(Duration.seconds(0), new KeyValue(lblAlerts.opacityProperty(), 1.0));
        KeyFrame endFadeText = new KeyFrame(Duration.seconds(1.5), new KeyValue(lblAlerts.opacityProperty(), 0.0));
        Timeline timelineText = new Timeline(startFadeText, endFadeText);
        timelineText.playFromStart();
    }


    private void endGame(boolean won) {
        charCanvas.setOnMouseClicked(null);
        Rectangle r = new Rectangle(1300, 845);
        r.setFill(Color.rgb(0,0,0));

        KeyFrame startFadeText = new KeyFrame(Duration.seconds(0), new KeyValue(r.opacityProperty(), 0.0));
        KeyFrame endFadeText = new KeyFrame(Duration.seconds(3), new KeyValue(r.opacityProperty(), 1.0));
        Timeline timelineText = new Timeline(startFadeText, endFadeText);
        timelineText.playFromStart();

        getChildren().add(r);
        Label lblEndGame = new Label();
        lblEndGame.setMinWidth(1300);
        lblEndGame.setFont(Font.loadFont("file:resources/fonts/StraightToHellSinnerBB.ttf", 80));
        lblEndGame.setWrapText(true);
        lblEndGame.setMinHeight(400);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2.0);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.BLACK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        shadow.setInput(lighting);

        lblEndGame.setTextFill(Color.WHITE);
        lblEndGame.setAlignment(Pos.CENTER);

        lblEndGame.setEffect(shadow);

        //Button btnEnd = new Button("Volver al menu");

        timer.stop();
        timeLeft.purge();

        AudioClip endSound;
        if(won) {
            lblEndGame.setText("Has triunfado sobre tu adversario!");
            endSound = new AudioClip(ClassLoader.getSystemResource("sounds/victory.wav").toString());
        } else {
            lblEndGame.setText("Has sido derrotado!");
            endSound = new AudioClip(ClassLoader.getSystemResource("sounds/defeat.wav").toString());
        }

        endSound.play();

        getChildren().addAll(lblEndGame);

    }


    /**
     * Constructor de la ventana
     * @param game instancia del objeto Game del backend
     * @param owner instancia del jugador en el back que es dueño de la ventana
     */
    Board(back.Game game, back.Player owner, boolean silent) {

        this.silent = silent;

        if(!silent) {
            Media bgSong = new Media(ClassLoader.getSystemResource("sounds/Battle.mp3").toString());
            mp = new MediaPlayer(bgSong);
            mp.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mp.seek(Duration.ZERO);
                }
            });
            mp.play();
        }



        ImageView background = new ImageView(new Image("/graphics/map/fondo.png", 1300, 820, false, false));

        HBox hb = new HBox(80);

        hb.setLayoutX(78);
        hb.setLayoutY(60);


        lblAlerts = new Label();

        lblAlerts.setTextFill(Color.WHITE);
        lblAlerts.setMaxWidth(700);
        lblAlerts.setMinWidth(700);
        lblAlerts.setAlignment(Pos.CENTER);
        lblAlerts.setFont(Font.loadFont("file:resources/fonts/Barbarian.ttf", 60));

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2.0);
        shadow.setOffsetY(2.0);
        shadow.setColor(Color.BLACK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);
        shadow.setInput(lighting);

        lblAlerts.setEffect(shadow);



        lblAlerts.setMouseTransparent(true);

        castle1Indicator = new ProgressBar();
        castle1Indicator.setProgress(1);
        castle1Indicator.setLayoutX(70);
        castle1Indicator.setLayoutY(790);
        castle1Indicator.setPrefSize(250, 15);

        castle2Indicator = new ProgressBar();
        castle2Indicator.setProgress(1);
        castle2Indicator.setLayoutX(70);
        castle2Indicator.setLayoutY(15);
        castle2Indicator.setPrefSize(250, 15);

        if (owner == game.getPlayer1()) {
            castle1Indicator.getStyleClass().add("myCastle");
            castle2Indicator.getStyleClass().add("notMyCastle");
        } else {
            castle1Indicator.getStyleClass().add("notMyCastle");
            castle2Indicator.getStyleClass().add("myCastle");
        }



        getChildren().addAll(background, hb, castle1Indicator, castle2Indicator);


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


        pBoard.getChildren().addAll(backgroundCanvas, charCanvas, lblAlerts);



        //addSoldier

        for(int i = 0; i < NUMROWS; i++)
            for(int j = 0; j < NUMCOLS; j++) {
            back.Soldier s = game.getSoldier(new Point(i, j));
                if(s != null)
                    tiles[i][j].setWhosHere(new GraphicSoldier(s, owner.equals(s.getOwner())));

            }

        VBox menu = createMenu();
        menu.setTranslateY(-45);



        hb.getChildren().addAll(pBoard, menu);


        charCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<>() {
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

        drawCardBtn.setDisable(true);
        endTurnBtn.setDisable(true);

        timeLeft = new Timer();

        timer.start();
    }

    /**
     * Método a ejecutarse cuando inicia un nuevo turno del jugador dueño de la ventana. Pone los controles en el estado apropiado.
     */
    private void ReflectStartTurn() {
        if(!silent) changeTurnSound.play();
        showAlertText("Comienza tu turno");
        updateActionsLeft();

        drawCardBtn.setDisable(false);
        endTurnBtn.setDisable(false);
        scrollTimeLeft.setDisable(false);

        timeLeft = new Timer();

        TimerTask task = new TimerTask() {
            private int remainingTime = 30;

            @Override
            public void run() {
                scrollTimeLeft.setProgress((double)remainingTime/30);
                if (remainingTime-- == 0) {
                    remainingTime = 30;
                    game.endTurn();
                }
            }
        };

        timeLeft.scheduleAtFixedRate(task, 0, 1000);
    }


    /**
     * Método a ejecutarse cuando termina el turno del jugador dueño de la ventana. Pone los controles en el estado apropiado.
     */
    private void ReflectEndTurn() {
        if(!silent) changeTurnSound.play();
        showAlertText("Turno de " + game.getCurrentPlayer().getName());
        drawCardBtn.setDisable(true);
        endTurnBtn.setDisable(true);
        scrollTimeLeft.setDisable(true);
        timeLeft.cancel();
        resetTileStatus();
    }

    /**
     * Convierte las coordenadas del canvas a un objeto punto que refiere al tile correspondiente.
     * @param x Abscisa relativa a la esquina superior izquierda del canvas
     * @param y Ordenada relativa a la esquina superior izquierda del canvas
     * @return Point que indica el tile sobre el que se clickeó
     */
    private Point getPointFromCoordinates(int x, int y) {
        int i = x / CELLSIZE;
        int j = y / CELLSIZE;

        return new Point(j, i);
    }

    /**
     * Borra el contenido de los canvas y ordena a cada uno de los tiles que se redibuje
     */
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
    /**
     * Timer que se accione por cada FPS. La variable fps apunta a regular la frecuencia con la que se redibuja
     * El cliente pregunta a la instancia del jugador (en el back) que acciones hace falta dibujar en pantalla
     */
    private AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (fps == 0 || fps == 5) {
                PendingDrawing action;

                while((action = owner.getActionRegistry()) != null) {

                    Tile origin = (action.getOrigin() != null) ? tiles[action.getOrigin().x][action.getOrigin().y] : null;
                    Tile dest = (action.getDestination() != null) ? tiles[action.getDestination().x][action.getDestination().y] : null;

                    switch(action.getType()) {
                        case MOVEMENT: //movimiento
                            if(origin == null) { //invocar
                                dest.setWhosHere(new GraphicSoldier((back.Soldier) action.getCard(), action.getCard().getOwner().equals(owner)));
                            } else if(dest == null) { //morir
                                origin.addCorpse();
                                origin.setWhosHere(null);

                            } else { //mover
                                dest.setWhosHere( origin.getWhosHere() );

                                dest.moveSoldier(new Point(dest.getPos().x - origin.getPos().x, dest.getPos().y - origin.getPos().y));

                                origin.setWhosHere(null);
                            }
                            break;
                        case ENDTURN:
                            ReflectEndTurn();
                            break;
                        case STARTTURN:
                            ReflectStartTurn();
                            break;
                        case RECEIVESPELL:

                            back.Magic c = (back.Magic) action.getCard();

                            dest.setMagic(c);

                            if(c.getIsNegative())
                                dest.setEffect( new TileEffect(7, 148, 0, 211));
                            else
                                dest.setEffect( new TileEffect(7, 127, 255, 211));

                            break;
                        case GETHIT:
                            dest.setEffect( new TileEffect(30, 255, 0, 0));
                            break;
                        case EVADE:
                            dest.setEffect( new TileEffect(30, 50, 50, 240 ));
                            break;
                        case STRIKE:
                            dest.setEffect( new TileEffect(30, 150, 20, 150 ));
                            break;
                        case WIN:
                            endGame(true);
                            break;
                        case LOSE:
                            endGame(false);
                            break;
                    }
                }
                draw();
                castle1Indicator.setProgress((double)game.getPlayer1().getCastle().getLife() / CASTLELIFE);
                castle2Indicator.setProgress((double)game.getPlayer2().getCastle().getLife() / CASTLELIFE);
                fps = 0;
            }
            fps++;
        }
    };
}
