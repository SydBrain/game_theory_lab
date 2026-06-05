package org.lab.service;

import org.lab.model.PayoffMatrix;
import org.lab.persistence.DatabaseManager;
import org.lab.simulation.TournamentResult;

public class TournamentService {

    private final DatabaseManager db;

    public TournamentService() {
        this.db = new DatabaseManager();
        this.db.initializeSchema();
    }

    public void save(TournamentResult result, PayoffMatrix matrix, int rounds) {
        db.saveTournament(result, matrix.getName(), rounds);
    }
}