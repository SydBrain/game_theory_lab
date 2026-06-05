package org.lab.controller;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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

    // ── Panes ──────────────────────────────────────────────────────────────
    @FXML private ScrollPane configPane;
    @FXML private ScrollPane resultsPane;

    // ── Config: game cards ─────────────────────────────────────────────────
    @FXML private VBox cardPrisoners;
    @FXML private VBox cardStagHunt;
    @FXML private VBox cardHawkDove;

    // ── Config: strategy chips ─────────────────────────────────────────────
    @FXML private ToggleButton chipCooperate;
    @FXML private ToggleButton chipDefect;
    @FXML private ToggleButton chipGrudger;
    @FXML private ToggleButton chipPavlov;
    @FXML private ToggleButton chipTitForTat;

    // ── Config: rounds + run ───────────────────────────────────────────────
    @FXML private Slider roundsSlider;
    @FXML private Label  roundsValue;
    @FXML private Button runButton;

    // ── Results: top bar ───────────────────────────────────────────────────
    @FXML private Label  resultsTitleLabel;
    @FXML private Button saveTournamentButton;

    // ── Results: Nash ──────────────────────────────────────────────────────
    @FXML private Label nashLabel;

    // ── Results: leaderboard ───────────────────────────────────────────────
    @FXML private TableView<Map.Entry<String, Integer>>      table;
    @FXML private TableColumn<Map.Entry<String, Integer>, String>  nameCol;
    @FXML private TableColumn<Map.Entry<String, Integer>, Integer> pointsCol;
    @FXML private BarChart<String, Number> chart;

    // ── Results: match details ─────────────────────────────────────────────
    @FXML private ComboBox<MatchResult>    matchSelector;
    @FXML private LineChart<Number, Number> lineChart;

    // ── Game data (matches card order) ─────────────────────────────────────
    private final PayoffMatrix prisonersDilemma = PayoffMatrix.prisonersDilemma();
    private final PayoffMatrix stagHunt         = PayoffMatrix.stagHunt();
    private final PayoffMatrix hawkDove         = PayoffMatrix.hawkDove();

    private final TournamentService tournamentService = new TournamentService();

    // ── State ──────────────────────────────────────────────────────────────
    private PayoffMatrix     selectedMatrix;
    private TournamentResult lastResult;
    private PayoffMatrix     lastMatrix;
    private int              lastRounds;

    @FXML
    public void initialize() {
        cardPrisoners.setOnMouseClicked(e -> selectGame(prisonersDilemma, cardPrisoners));
        cardStagHunt.setOnMouseClicked(e  -> selectGame(stagHunt,         cardStagHunt));
        cardHawkDove.setOnMouseClicked(e  -> selectGame(hawkDove,         cardHawkDove));

        roundsSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                roundsValue.setText(String.valueOf(newVal.intValue()))
        );

        chipCooperate.selectedProperty().addListener((obs, o, n) -> updateRunButton());
        chipDefect.selectedProperty().addListener((obs, o, n)    -> updateRunButton());
        chipGrudger.selectedProperty().addListener((obs, o, n)   -> updateRunButton());
        chipPavlov.selectedProperty().addListener((obs, o, n)    -> updateRunButton());
        chipTitForTat.selectedProperty().addListener((obs, o, n) -> updateRunButton());

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

    // ── Card selection ─────────────────────────────────────────────────────

    private void selectGame(PayoffMatrix matrix, VBox card) {
        selectedMatrix = matrix;
        cardPrisoners.getStyleClass().remove("game-card-selected");
        cardStagHunt.getStyleClass().remove("game-card-selected");
        cardHawkDove.getStyleClass().remove("game-card-selected");
        card.getStyleClass().add("game-card-selected");
        updateRunButton();
    }

    private void updateRunButton() {
        int count = 0;
        if (chipCooperate.isSelected()) count++;
        if (chipDefect.isSelected())    count++;
        if (chipGrudger.isSelected())   count++;
        if (chipPavlov.isSelected())    count++;
        if (chipTitForTat.isSelected()) count++;
        runButton.setDisable(selectedMatrix == null || count < 2);
    }

    // ── Tournament run ─────────────────────────────────────────────────────

    @FXML
    private void onRunTournament() {
        List<Agent> agents = new ArrayList<>();
        if (chipCooperate.isSelected()) agents.add(new AlwaysCooperate());
        if (chipDefect.isSelected())    agents.add(new AlwaysDefect());
        if (chipGrudger.isSelected())   agents.add(new Grudger());
        if (chipPavlov.isSelected())    agents.add(new Pavlovian());
        if (chipTitForTat.isSelected()) agents.add(new TitForTat());

        if (agents.size() < 2 || selectedMatrix == null) return;

        int rounds = (int) roundsSlider.getValue();
        Tournament tournament = new Tournament();
        TournamentResult result = tournament.runTournament(agents, selectedMatrix, rounds);

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
        List<Move[]> equilibria = nash.findPureNashEquilibria(selectedMatrix);
        StringBuilder sb = new StringBuilder("Nash Equilibria: ");
        for (Move[] eq : equilibria) {
            sb.append("(").append(eq[0]).append(", ").append(eq[1]).append(") ");
        }
        nashLabel.setText(sb.toString());

        resultsTitleLabel.setText(selectedMatrix.getName() + "  ·  " + rounds + " rounds");

        lastResult = result;
        lastMatrix = selectedMatrix;
        lastRounds = rounds;
        saveTournamentButton.setText("Save Tournament");
        saveTournamentButton.setDisable(false);

        showResults();
    }

    // ── Navigation ─────────────────────────────────────────────────────────

    @FXML
    private void onNewTournament() {
        showConfig();
    }

    private void showConfig() {
        resultsPane.setVisible(false);
        resultsPane.setManaged(false);
        configPane.setVisible(true);
        configPane.setManaged(true);
    }

    private void showResults() {
        // Show resultsPane on top of configPane, then hide configPane once opaque.
        resultsPane.setOpacity(0);
        resultsPane.setVisible(true);
        resultsPane.setManaged(true);
        FadeTransition ft = new FadeTransition(Duration.millis(250), resultsPane);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setOnFinished(e -> {
            configPane.setVisible(false);
            configPane.setManaged(false);
        });
        ft.play();
    }

    // ── Save ───────────────────────────────────────────────────────────────

    @FXML
    private void onSaveTournament() {
        tournamentService.save(lastResult, lastMatrix, lastRounds);
        saveTournamentButton.setDisable(true);
        saveTournamentButton.setText("Saved ✓");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> saveTournamentButton.setText("Save Tournament"));
        pause.play();
    }

    // ── Chart ──────────────────────────────────────────────────────────────

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
