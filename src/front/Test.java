package front;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        back.Game game = new back.Game("Eze", "Mike");

        Board board = new Board(game, game.getPlayer1());
        //Board board2 = new Board(game, game.getPlayer2());

        Scene scene1 = new Scene(board);
        //Scene scene2 = new Scene(board2);

        scene1.getStylesheets().add("css/scrollbar.css");
        //scene2.getStylesheets().add("css/scrollbar.css");

        /*Stage secondStage = new Stage();
        secondStage.setScene(scene2);
        secondStage.show();*/

        primaryStage.setScene(scene1);
        primaryStage.show();
    }

}