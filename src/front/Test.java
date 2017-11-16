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

        Stage firstStage = new Stage();
        //primaryStage.setHeight(); PENDIENTE!!!
        firstStage.setResizable(false);
        StartWindow startWindow = new StartWindow(firstStage);
        Scene scene1 = new Scene(startWindow);
        firstStage.setScene(scene1);

        firstStage.show();
    }

}