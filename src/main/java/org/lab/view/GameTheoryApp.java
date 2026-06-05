package org.lab.view;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.lab.model.*;
import org.lab.simulation.PrisonerPayoffMatrix;
import org.lab.simulation.Tournament;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameTheoryApp extends Application {

    @Override
    public void start(Stage stage) {

        VBox vbox = new VBox(8);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Button startButton = new Button(HomeLabels.runTournamentLabel);

        TableView<Map.Entry<String, Integer>> table = new TableView<>();

        TableColumn<Map.Entry<String, Integer>, String> nameCol = new TableColumn<>(HomeLabels.strategy);
        nameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getKey())
        );

        TableColumn<Map.Entry<String, Integer>, Integer> pointsCol = new TableColumn<>(HomeLabels.points);
        pointsCol.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getValue()).asObject()
        );

        table.getColumns().addAll(nameCol, pointsCol);

        startButton.setOnAction(event -> {
            Map<String, Integer> leaderboard = runTournament();
            table.getItems().setAll(leaderboard.entrySet());
        });

        vbox.getChildren().addAll(startButton, table);

        Scene scene = new Scene(vbox, 640, 480);
        scene.getRoot().setStyle("-fx-base: #1e1e1e; -fx-background: #1e1e1e;");
        stage.setTitle("Game Theory Lab");
        stage.setScene(scene);
        stage.show();
    }

    private LinkedHashMap<String, Integer> runTournament() {
        Tournament tournament = new Tournament();
        PrisonerPayoffMatrix matrix = new PrisonerPayoffMatrix();

        List<Agent> agents = List.of(
                new AlwaysCooperate(),
                new AlwaysDefect(),
                new Grudger(),
                new Pavlovian(),
                new TitForTat()
        );

        return tournament.runTournament(agents, matrix, 10);
    }

    public static void main(String[] args) {
        launch(args);
    }
}