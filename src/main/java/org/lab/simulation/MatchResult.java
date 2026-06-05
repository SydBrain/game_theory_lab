package org.lab.simulation;

import java.util.List;

public class MatchResult {
    private final String playerAName;
    private final String playerBName;
    private final List<Integer> playerAScores;
    private final List<Integer> playerBScores;

    public MatchResult(String playerAName, String playerBName, List<Integer> playerAScores, List<Integer> playerBScores) {
        this.playerAName = playerAName;
        this.playerBName = playerBName;
        this.playerAScores = playerAScores;
        this.playerBScores = playerBScores;
    }

    @Override
    public String toString() {
        return playerAName + " vs " + playerBName;
    }

    public String getPlayerAName() { return playerAName; }
    public String getPlayerBName() { return playerBName; }
    public List<Integer> getPlayerAScores() { return playerAScores; }
    public List<Integer> getPlayerBScores() { return playerBScores ; }
}