package org.lab.model;

public class Pavlovian extends Agent {

    public Pavlovian() {
        super.setName("Pavlovian");
    }

    @Override
    public Move play(Move lastOpponentMove) {
        if (this.moveHistory.isEmpty() || (this.getMoveHistory().getLast() == lastOpponentMove)) {
            return Move.COOPERATE;
        } else return Move.DEFECT;
    }
}
