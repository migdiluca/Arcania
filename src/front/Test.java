package front;

import back.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {

        File file = null;

        back.Game game = new back.Game("Eze", "Mike");

        Board board = new Board(game, game.getPlayer1(),primaryStage);
        Board board2 = new Board(game, game.getPlayer2(),primaryStage);

        Scene scene1 = new Scene(board);
        Scene scene2 = new Scene(board2);

        scene1.getStylesheets().add("css/scrollbar.css");
        scene2.getStylesheets().add("css/scrollbar.css");

        Stage secondStage = new Stage();
        secondStage.setResizable(false);
        secondStage.setTitle(game.getPlayer2().getName() + " - Arcania");
        secondStage.setScene(scene2);
        secondStage.show();


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

        primaryStage.setScene(scene1);
        //primaryStage.setHeight(); PENDIENTE!!!
        primaryStage.setResizable(false);
        primaryStage.setTitle(game.getPlayer1().getName() + " - Arcania");
        primaryStage.show();


    }

}