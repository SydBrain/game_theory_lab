package org.lab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Agent implements Strategy {

    private String name;
    protected int points;
    protected List<Move> moveHistory;

    public Agent() {
        this.name = "";
        this.points = 0;
        this.moveHistory = new ArrayList<>();
    }

    public void addPoints(int points) {
        this.points += points;
    }

    @Override
    public abstract Move play(Move lastOpponentMove);

    public void updateMoveHistory(Move newMove) {
        this.moveHistory.add(newMove);
    }

    public void reset() {
        this.points = 0;
        this.moveHistory.clear();
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getPoints() { return points; }
    public List<Move> getMoveHistory() { return Collections.unmodifiableList(moveHistory); }
}
