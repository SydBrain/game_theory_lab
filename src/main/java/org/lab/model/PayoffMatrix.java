package org.lab.model;

public class PayoffMatrix {
    private final String name;
    private final Payoff[][] matrix;

    public PayoffMatrix(String name, Payoff[][] matrix) {
        this.name = name;
        this.matrix = matrix;
    }

    public Payoff getPayoff(Move moveA, Move moveB) {
        return matrix[moveA.ordinal()][moveB.ordinal()];
    }

    public Move[] getPlayerAMoves() {
        return Move.values();
    }

    public Move[] getPlayerBMoves() {
        return Move.values();
    }

    public static PayoffMatrix prisonersDilemma() {
        return new PayoffMatrix("Prisoner's Dilemma", new Payoff[][] {
                { new Payoff(3,3), new Payoff(0,5) },
                { new Payoff(5,0), new Payoff(1,1) }
        });
    }

    public static PayoffMatrix stagHunt() {
        return new PayoffMatrix("Stag Hunt", new Payoff[][] {
                { new Payoff(4,4), new Payoff(0,3) },
                { new Payoff(3,0), new Payoff(2,2) }
        });
    }

    public static PayoffMatrix hawkDove() {
        return new PayoffMatrix("Hawk-Dove", new Payoff[][] {
                { new Payoff(3,3), new Payoff(1,5) },
                { new Payoff(5,1), new Payoff(0,0) }
        });
    }

    public String getName() { return name; }
    public int getRowCount() { return matrix.length; }
    public int getColCount() { return matrix[0].length; }
    public Payoff getPayoffAt(int row, int col) { return matrix[row][col]; }
}