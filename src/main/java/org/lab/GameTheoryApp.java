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
        scene.getRoot().setStyle("-fx-base: #1e1e1e; -fx-background: #1e1e1e;");
        stage.setTitle("Game Theory Lab");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}