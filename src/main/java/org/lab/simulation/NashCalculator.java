package org.lab.simulation;

import org.lab.model.Move;
import org.lab.model.Payoff;
import org.lab.model.PayoffMatrix;

import java.util.ArrayList;
import java.util.List;

public class NashCalculator {

    public List<Move[]> findPureNashEquilibria(PayoffMatrix matrix) {
        List<Move[]> equilibria = new ArrayList<>();

        for (Move moveA : matrix.getPlayerAMoves()) {
            for (Move moveB : matrix.getPlayerBMoves()) {

                if (isBestResponseForA(matrix, moveA, moveB) && isBestResponseForB(matrix, moveA, moveB)) {
                    equilibria.add(new Move[]{moveA, moveB});
                }
            }
        }

        return equilibria;
    }

    private boolean isBestResponseForA(PayoffMatrix matrix, Move currentMoveA, Move moveB) {
        int currentPayoff = matrix.getPayoff(currentMoveA, moveB).aPlayerPoints();

        for (Move alternativeMoveA : matrix.getPlayerAMoves()) {
            if (matrix.getPayoff(alternativeMoveA, moveB).aPlayerPoints() > currentPayoff) {
                return false;
            }
        }
        return true;
    }

    private boolean isBestResponseForB(PayoffMatrix matrix, Move moveA, Move currentMoveB) {
        int currentPayoff = matrix.getPayoff(moveA, currentMoveB).bPlayerPoints();

        for (Move alternativeMoveB : matrix.getPlayerBMoves()) {
            if (matrix.getPayoff(moveA, alternativeMoveB).bPlayerPoints() > currentPayoff) {
                return false;
            }
        }
        return true;
    }
}