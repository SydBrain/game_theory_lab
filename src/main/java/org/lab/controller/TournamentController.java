package org.lab.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.lab.model.*;
import org.lab.model.PayoffMatrix;
import org.lab.service.TournamentService;
import org.lab.simulation.MatchResult;
import org.lab.simulation.NashCalculator;
import org.lab.simulation.Tournament;
import org.lab.simulation.TournamentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TournamentController {

    @FXML private ComboBox<PayoffMatrix> gameSelector;
    @FXML private CheckBox cbCooperate, cbDefect, cbGrudger, cbPavlov, cbTitForTat;
    @FXML private TableView<Map.Entry<String, Integer>> table;
    @FXML private TableColumn<Map.Entry<String, Integer>, String> nameCol;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> pointsCol;
    @FXML private BarChart<String, Number> chart;
    @FXML private Label nashLabel;
    @FXML private Slider roundsSlider;
    @FXML private Label roundsValue;
    @FXML private ComboBox<MatchResult> matchSelector;
    @FXML private LineChart<Number, Number> lineChart;

    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {

        gameSelector.getItems().addAll(
                PayoffMatrix.prisonersDilemma(),
                PayoffMatrix.stagHunt(),
                PayoffMatrix.hawkDove()
        );

        roundsSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            roundsValue.setText(String.valueOf(newVal.intValue()));
        });

        nameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getKey())
        );

        pointsCol.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getValue()).asObject()
        );

        matchSelector.setOnAction(event -> {
            MatchResult selected = matchSelector.getValue();
            if (selected == null) return;
            drawLineChart(selected);
        });
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
        PayoffMatrix matrix = gameSelector.getValue();
        if (matrix == null) return;

        int rounds = (int) roundsSlider.getValue();

        TournamentResult result = tournament.runTournament(agents, matrix, rounds);

        table.getItems().setAll(result.getLeaderboard().entrySet());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Points");
        result.getLeaderboard().forEach((name, points) ->
                series.getData().add(new XYChart.Data<>(name, points))
        );
        chart.getData().clear();
        chart.getData().add(series);

        matchSelector.getItems().clear();
        matchSelector.getItems().addAll(result.getMatchResults());

        NashCalculator nash = new NashCalculator();
        List<Move[]> equilibria = nash.findPureNashEquilibria(matrix);

        StringBuilder sb = new StringBuilder("Nash Equilibria: ");

        for (Move[] eq : equilibria) {
            sb.append("(").append(eq[0]).append(", ").append(eq[1]).append(") ");
        }

        nashLabel.setText(sb.toString());

        tournamentService.save(result, matrix, rounds);
    }

    private void drawLineChart(MatchResult match) {
        lineChart.getData().clear();

        XYChart.Series<Number, Number> seriesA = new XYChart.Series<>();
        seriesA.setName(match.getPlayerAName());

        XYChart.Series<Number, Number> seriesB = new XYChart.Series<>();
        seriesB.setName(match.getPlayerBName());

        for (int i = 0; i < match.getPlayerAScores().size(); i++) {
            seriesA.getData().add(new XYChart.Data<>(i, match.getPlayerAScores().get(i)));
            seriesB.getData().add(new XYChart.Data<>(i, match.getPlayerBScores().get(i)));
        }

        lineChart.getData().addAll(seriesA, seriesB);
    }
}