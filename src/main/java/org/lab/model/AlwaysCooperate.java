package org.lab.model;

public class AlwaysCooperate extends Agent {

    public AlwaysCooperate() {
        super.setName("Always Cooperate");
    }

    @Override
    public Move play(Move lastOpponentMove) {
        return Move.COOPERATE;
    }
}
