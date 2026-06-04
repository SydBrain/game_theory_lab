package org.lab.model;

public class AlwaysDefect extends Agent {

    public AlwaysDefect() {
        super.setName("Always Defect");
    }

    @Override
    public Move play(Move lastOpponentMove) {
        return Move.DEFECT;
    }

}
