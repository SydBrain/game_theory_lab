package org.lab.simulation;

import org.lab.model.Agent;
import org.lab.model.Move;

import java.util.*;
import java.util.stream.Collectors;

public class Tournament {

    public void runMatch(Agent a, Agent b, PrisonerPayoffMatrix matrix, int rounds) {
        System.out.println("Match between " + a.getName() + " and " + b.getName());

        Move lastMoveA = null;
        Move lastMoveB = null;

        Payoff turnPayoff;

        for (int i = 0; i < rounds; i++) {

            Move currentA = a.play(lastMoveB);
            Move currentB = b.play(lastMoveA);

            System.out.println("Round " + i);
            System.out.println("A move: " + currentA);
            System.out.println("B move: " + currentB);

            lastMoveA = currentA;
            lastMoveB = currentB;

            a.updateMoveHistory(lastMoveA);
            b.updateMoveHistory(lastMoveB);

            turnPayoff = matrix.getPayoff(lastMoveA, lastMoveB);

            a.addPoints(turnPayoff.aPlayerPoints());
            b.addPoints(turnPayoff.bPlayerPoints());

            System.out.println("Round " + (i + 1) + " concluded.");
            System.out.println(a.getName() + " points = " + a.getPoints());
            System.out.println(b.getName() + " points = " + b.getPoints());

        }

        if (a.getPoints() > b.getPoints()) {
            System.out.println("Player " + a.getName() + " wins");
        } else if (a.getPoints() < b.getPoints()) {
            System.out.println("Player " + b.getName() + " wins");
        } else {
            System.out.println("It's a DRAW!");
        }


    }

    public LinkedHashMap<String, Integer> runTournament(List<Agent> agents, PrisonerPayoffMatrix matrix, int rounds) {
        List<Matchup> matchups = createMatchups(agents);
        Map<String, Integer> leaderboard = new HashMap<>();

        for (Agent a: agents) {
            leaderboard.put(a.getName(), a.getPoints());
        }

        for (Matchup m : matchups) {
            runMatch(m.playerA, m.playerB, matrix, rounds);

            leaderboard.merge(m.playerA.getName(), m.playerA.getPoints(), Integer::sum);
            leaderboard.merge(m.playerB.getName(), m.playerB.getPoints(), Integer::sum);

            m.playerA.reset();
            m.playerB.reset();
        }

        return leaderboard.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public List<Matchup> createMatchups(List<Agent> agents) {

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
}
