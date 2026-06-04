package org.lab.model;

public class Grudger extends Agent {

    private boolean opponentHasDefected;

    public Grudger() {
        super.setName("Grudger");
    }

    @Override
    public Move play(Move lastOpponentMove) {
        if (lastOpponentMove == Move.DEFECT) {
            this.opponentHasDefected = true;
        }

        if (opponentHasDefected) {
            return Move.DEFECT;
        } else return Move.COOPERATE;

    }

    @Override
    public void reset() {
        super.reset();
        this.opponentHasDefected = false;
    }
}
