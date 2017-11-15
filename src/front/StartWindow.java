package front;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.reflect.annotation.ExceptionProxy;

import java.io.File;
import java.io.IOException;


public class StartWindow extends VBox {

    private Stage myStage;

    StartWindow(Stage myStage) {

        this.myStage = myStage;

        Button startBtn = new Button("Iniciar partida");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                createNewGame("Eze", "Mike");

            }
        });

        Button loadBtn = new Button("Cargar partida");
        loadBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                loadGame("Eze", "Mike");

            }
        });

        getChildren().addAll(startBtn, loadBtn);
    }

    private void loadScenes(String name1, String name2, back.Game game) {
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

    public void handleClosing(Stage stage, back.Game game) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        loadScenes(name1, name2, game);

    }

}
