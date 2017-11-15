package front;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class StartWindow extends VBox {

    private Stage myStage;

    StartWindow(Stage myStage) {

        this.myStage = myStage;

        Button startBtn = new Button("Iniciar partida");

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                startGame("Eze", "Mike");

            }
        });

        getChildren().addAll(startBtn);
    }

    private void startGame(String name1, String name2) {

        back.Game game = new back.Game(name1, name2);

        Stage firstStage = new Stage();
        //primaryStage.setHeight(); PENDIENTE!!!
        firstStage.setResizable(false);
        firstStage.setTitle(game.getPlayer1().getName() + " - Arcania");
        Board board = new Board(game, game.getPlayer1(),firstStage);
        Scene scene1 = new Scene(board);
        firstStage.setScene(scene1);
        scene1.getStylesheets().add("css/scrollbar.css");




        Stage secondStage = new Stage();
        secondStage.setResizable(false);
        secondStage.setTitle(name2 + " - Arcania");
        Board board2 = new Board(game, game.getPlayer2(),secondStage);
        Scene scene2 = new Scene(board2);
        secondStage.setScene(scene2);
        scene2.getStylesheets().add("css/scrollbar.css");




        firstStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        firstStage.show();
        secondStage.show();

        myStage.close();

    }

}
