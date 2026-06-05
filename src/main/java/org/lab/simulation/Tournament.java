package org.lab.simulation;

import org.lab.model.Agent;
import org.lab.model.Move;
import org.lab.model.Payoff;
import org.lab.model.PayoffMatrix;

import java.util.*;
import java.util.stream.Collectors;

public class Tournament {

    private List<Matchup> createMatchups(List<Agent> agents) {

        List<Matchup> matchups = new ArrayList<>();

        for (int i = 0; i < agents.size(); i++) {

            for (int j = i + 1; j < agents.size(); j++) {

                Agent playerA = agents.get(i);
                Agent playerB = agents.get(j);

                if (playerA != playerB) {
                    Matchup currentMatchup = new Matchup(playerA, playerB);
                    matchups.add(currentMatchup);
                }
            }
        }

        return matchups;
    }

    private record Matchup(Agent playerA, Agent playerB) {}

    public MatchResult runMatch(Agent a, Agent b, PayoffMatrix matrix, int rounds) {

        List<Integer> playerAScores = new ArrayList<>();
        List<Integer> playerBScores = new ArrayList<>();

        Move lastMoveA = null;
        Move lastMoveB = null;

        Payoff turnPayoff;

        for (int i = 0; i < rounds; i++) {

            Move currentA = a.play(lastMoveB);
            Move currentB = b.play(lastMoveA);

            lastMoveA = currentA;
            lastMoveB = currentB;

            a.updateMoveHistory(lastMoveA);
            b.updateMoveHistory(lastMoveB);

            turnPayoff = matrix.getPayoff(lastMoveA, lastMoveB);

            a.addPoints(turnPayoff.aPlayerPoints());
            b.addPoints(turnPayoff.bPlayerPoints());

            playerAScores.add(a.getPoints());
            playerBScores.add(b.getPoints());

        }

        return new MatchResult(a.getName(), b.getName(), playerAScores, playerBScores);
    }

    public TournamentResult runTournament(List<Agent> agents, PayoffMatrix matrix, int rounds) {

        List<Matchup> matchups = createMatchups(agents);

        List<MatchResult> matchResults = new ArrayList<>();

        Map<String, Integer> leaderboard = new HashMap<>();

        for (Agent a: agents) {
            leaderboard.put(a.getName(), a.getPoints());
        }

        for (Matchup m : matchups) {

            MatchResult currentMatchResult;

            currentMatchResult = runMatch(m.playerA, m.playerB, matrix, rounds);
            matchResults.add(currentMatchResult);

            leaderboard.merge(m.playerA.getName(), m.playerA.getPoints(), Integer::sum);
            leaderboard.merge(m.playerB.getName(), m.playerB.getPoints(), Integer::sum);

            m.playerA.reset();
            m.playerB.reset();
        }

        LinkedHashMap<String, Integer> orderedLeaderboard;

        orderedLeaderboard = leaderboard.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return new TournamentResult(matrix.getName(), matchResults, orderedLeaderboard);
    }


}
