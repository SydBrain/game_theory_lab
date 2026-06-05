package org.lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameTheoryApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lab/view/tournament-view.fxml"));
        Scene scene = new Scene(loader.load(), 900, 700);
        scene.getStylesheets().add(getClass().getResource("/org/lab/view/style.css").toExternalForm());
        stage.setTitle("Game Theory Lab");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}