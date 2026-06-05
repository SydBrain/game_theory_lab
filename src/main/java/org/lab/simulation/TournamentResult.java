package org.lab.simulation;

import java.util.LinkedHashMap;
import java.util.List;

public class TournamentResult {
    private final String gameName;
    private final List<MatchResult> matchResults;
    private final LinkedHashMap<String, Integer> leaderboard;

    public TournamentResult(String gameName, List<MatchResult> matchResults, LinkedHashMap<String, Integer> leaderboard) {
        this.gameName = gameName;
        this.matchResults = matchResults;
        this.leaderboard = leaderboard;
    }

    public List<MatchResult> getMatchResults() { return matchResults; }
    public LinkedHashMap<String, Integer> getLeaderboard() { return leaderboard; }
}
