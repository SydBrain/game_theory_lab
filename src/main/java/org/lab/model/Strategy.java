package org.lab.model;

public interface Strategy {
    Move play(Move lastOpponentMove);
}
