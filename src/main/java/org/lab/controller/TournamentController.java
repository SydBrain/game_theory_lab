package org.lab.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.lab.model.*;
import org.lab.model.PayoffMatrix;
import org.lab.simulation.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TournamentController {

    @FXML private CheckBox cbCooperate, cbDefect, cbGrudger, cbPavlov, cbTitForTat;
    @FXML private TableView<Map.Entry<String, Integer>> table;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> nameCol;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> pointsCol;
    @FXML private BarChart<String, Number> chart;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getKey())
        );
        pointsCol.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getValue()).asObject()
        );
    }

    @FXML
    private void onRunTournament() {
        List<Agent> agents = new ArrayList<>();
        if (cbCooperate.isSelected()) agents.add(new AlwaysCooperate());
        if (cbDefect.isSelected())    agents.add(new AlwaysDefect());
        if (cbGrudger.isSelected())   agents.add(new Grudger());
        if (cbPavlov.isSelected())    agents.add(new Pavlovian());
        if (cbTitForTat.isSelected()) agents.add(new TitForTat());

        if (agents.size() < 2) return;

        Tournament tournament = new Tournament();
        PayoffMatrix matrix = PayoffMatrix.prisonersDilemma();
        Map<String, Integer> leaderboard = tournament.runTournament(agents, matrix, 10);

        table.getItems().setAll(leaderboard.entrySet());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Points");
        leaderboard.forEach((name, points) ->
                series.getData().add(new XYChart.Data<>(name, points))
        );
        chart.getData().clear();
        chart.getData().add(series);
    }
}