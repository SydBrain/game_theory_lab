package org.lab.persistence;

import org.lab.simulation.TournamentResult;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:gametheorylab.db";

    private static final String[] SCHEMA_FILES = {
            "create_tournament.sql",
            "create_leaderboard_entry.sql",
            "create_match_result.sql"
    };

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void initializeSchema() {
        String[] schemas = {
                """
        CREATE TABLE IF NOT EXISTS TOURNAMENT (
            id INTEGER PRIMARY KEY,
            game_name TEXT NOT NULL,
            n_rounds INT CHECK(n_rounds > 0),
            happened_on TEXT NOT NULL
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS LEADERBOARD_ENTRY (
            id INTEGER PRIMARY KEY,
            tournament_id INT,
            strategy TEXT NOT NULL,
            total_points INT,
            FOREIGN KEY (tournament_id) REFERENCES TOURNAMENT(id)
            ON DELETE CASCADE ON UPDATE CASCADE
        )
        """,
                """
        CREATE TABLE IF NOT EXISTS MATCH_RESULT (
            id INTEGER PRIMARY KEY,
            tournament_id INT,
            player_a TEXT NOT NULL,
            player_b TEXT NOT NULL,
            score_a INT,
            score_b INT,
            FOREIGN KEY (tournament_id) REFERENCES TOURNAMENT(id)
            ON DELETE CASCADE ON UPDATE CASCADE
        )
        """
        };

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            for (String sql : schemas) {
                stmt.execute(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTournament(TournamentResult result, String gameName, int rounds) {

        String insertTournament = "INSERT INTO TOURNAMENT (game_name, n_rounds, happened_on) VALUES (?, ?, ?)";
        String insertLeaderboard = "INSERT INTO LEADERBOARD_ENTRY (tournament_id, strategy, total_points) VALUES (?, ?, ?)";
        String insertMatch = "INSERT INTO MATCH_RESULT (tournament_id, player_a, player_b, score_a, score_b) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {

            PreparedStatement pstmt = conn.prepareStatement(insertTournament, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, gameName);
            pstmt.setInt(2, rounds);
            pstmt.setString(3, java.time.LocalDate.now().toString());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            keys.next();
            int tournamentId = keys.getInt(1);

            PreparedStatement lbStmt = conn.prepareStatement(insertLeaderboard);
            for (var entry : result.getLeaderboard().entrySet()) {
                lbStmt.setInt(1, tournamentId);
                lbStmt.setString(2, entry.getKey());
                lbStmt.setInt(3, entry.getValue());
                lbStmt.executeUpdate();
            }

            PreparedStatement mrStmt = conn.prepareStatement(insertMatch);
            for (var match : result.getMatchResults()) {
                mrStmt.setInt(1, tournamentId);
                mrStmt.setString(2, match.getPlayerAName());
                mrStmt.setString(3, match.getPlayerBName());
                mrStmt.setInt(4, match.getPlayerAScores().getLast());
                mrStmt.setInt(5, match.getPlayerBScores().getLast());
                mrStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String loadSql(String filename) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("sql/schema" + filename)) {
            return new String(is.readAllBytes());
        }
    }


}
