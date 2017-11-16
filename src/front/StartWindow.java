package front;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class StartWindow extends StackPane {

    private Stage myStage;
    private final Random RNG = new Random();
    private static final int WINDOWSIZE = 500;

    StartWindow(Stage myStage) {

        this.myStage = myStage;

        myStage.setWidth(WINDOWSIZE);
        myStage.setHeight(WINDOWSIZE);
        myStage.setTitle("Arcania");

        ImageView background = new ImageView(new Image("/graphics/ui/login.png", WINDOWSIZE, WINDOWSIZE, false, true));
        TextField hidden = new TextField();

        getChildren().addAll(hidden, background, createFog());

        VBox form = new VBox(10);

        TextField name1 = new TextField();
        name1.setMaxWidth(200);
        name1.setAlignment(Pos.CENTER);
        name1.setPromptText("Nombre del Jugador 1");
        TextField name2 = new TextField();
        name2.setMaxWidth(200);
        name2.setAlignment(Pos.CENTER);
        name2.setPromptText("Nombre del Jugador 2");

        Button startBtn = new Button("Iniciar partida");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!name1.getText().equals("") && !name2.getText().equals("")) createNewGame(name1.getText(), name2.getText());

            }
        });

        Button loadBtn = new Button("Cargar partida");
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!name1.getText().equals("") && !name2.getText().equals("")) loadGame(name1.getText(), name2.getText());

            }
        });

        HBox h = new HBox(19);
        h.getChildren().addAll(startBtn, loadBtn);
        h.setAlignment(Pos.CENTER);

        form.getChildren().addAll(name1, name2, h);
        form.setAlignment(Pos.CENTER);
        getChildren().add(form);
    }

    private void loadScenes(String name1, String name2, back.Game game) {
        Stage firstStage = new Stage();
        firstStage.setHeight(845);
        firstStage.setWidth(1300);
        firstStage.setResizable(false);
        firstStage.setTitle(name1 + " - Arcania");
        Board board = new Board(game, game.getPlayer1());
        Scene scene1 = new Scene(board);
        firstStage.setScene(scene1);
        scene1.getStylesheets().add("css/scrollbar.css");


        Stage secondStage = new Stage();
        secondStage.setResizable(false);
        secondStage.setHeight(845);
        secondStage.setWidth(1300);
        secondStage.setTitle(name2 + " - Arcania");
        Board board2 = new Board(game, game.getPlayer2());
        Scene scene2 = new Scene(board2);
        secondStage.setScene(scene2);
        scene2.getStylesheets().add("css/scrollbar.css");


        firstStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                handleClosing(firstStage, game);
            }
        });

        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                handleClosing(secondStage, game);
            }
        });

        secondStage.show();
        firstStage.show();

        myStage.close();
    }

    private void handleClosing(Stage stage, back.Game game) {
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(stage);
            if(selectedFile != null)
                game.writeGame(selectedFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.exit();
        System.exit(0);
    }

    private void createNewGame(String name1, String name2) {
        back.Game game = new back.Game(name1, name2);
        loadScenes(name1, name2, game);
    }

    private void loadGame(String name1, String name2) {
        back.Game game = new back.Game(name1, name2);
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(myStage);
            if(selectedFile == null) return;

            game.loadGame(selectedFile);
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadScenes(name1, name2, game);

    }

    public Pane createFog() {
        Pane fog = new Pane();
        Rectangle rect = new Rectangle(0, 0, WINDOWSIZE, WINDOWSIZE);
        rect.setFill(Color.rgb(0xe0, 0xe0, 0xe0, 0.5));

        fog.getChildren().add(rect);

        for (int i = 0; i < 70; i++) {
            fog.getChildren().add(createFogElement());
        }

        fog.setEffect(new GaussianBlur((2 * WINDOWSIZE) / 2.5));
        return fog;
    }

    private Circle createFogElement() {
        Circle circle = new Circle(RNG.nextInt(WINDOWSIZE - 50) + 25, RNG.nextInt(WINDOWSIZE - 50) + 25, 15 + RNG.nextInt(50));
        int shade = 0xcf + RNG.nextInt(0x20);
        circle.setFill(Color.rgb(shade, shade, shade, 0.8));
        AnimationTimer anim = new AnimationTimer() {

            double xVel = RNG.nextDouble()*40;
            double yVel = RNG.nextDouble()*40;

            long lastUpdate = 0 ;

            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0 ;
                    double x = circle.getCenterX() ;
                    double y = circle.getCenterY() ;
                    if ( x + elapsedSeconds * xVel > WINDOWSIZE || x + elapsedSeconds * xVel < 0) {
                        xVel = - xVel ;
                    }
                    if ( y + elapsedSeconds * yVel > WINDOWSIZE || y + elapsedSeconds * yVel < 0) {
                        yVel = - yVel ;
                    }
                    circle.setCenterX(x + elapsedSeconds*xVel);
                    circle.setCenterY(y + elapsedSeconds * yVel);
                }
                lastUpdate = now ;
            }

        };
        anim.start();
        return circle ;
    }

}
