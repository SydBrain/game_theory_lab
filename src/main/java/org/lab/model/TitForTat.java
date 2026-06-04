package org.lab.model;

public class TitForTat extends Agent{

    public TitForTat() {
        super.setName("Tit For Tat");
    }

    @Override
    public Move play(Move lastOpponentMove) {
        if (lastOpponentMove == null) { return Move.COOPERATE; }
        return lastOpponentMove;
    }

}
