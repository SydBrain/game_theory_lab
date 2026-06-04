package org.lab.simulation;

import org.lab.model.Move;

public class PrisonerPayoffMatrix {
    private Payoff[][] payoffMatrix;

    public PrisonerPayoffMatrix() {
        this.payoffMatrix = new Payoff[2][2];
        this.payoffMatrix[0][0] = new Payoff(3,3);
        this.payoffMatrix[0][1] = new Payoff(0,5);

        this.payoffMatrix[1][0] = new Payoff(5,0);
        this.payoffMatrix[1][1] = new Payoff(1,1);
    }

    public Payoff getPayoff(Move moveA, Move moveB) {
        return this.payoffMatrix[moveA.ordinal()][moveB.ordinal()];
    }

}
