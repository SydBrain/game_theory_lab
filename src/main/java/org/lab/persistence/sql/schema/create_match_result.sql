CREATE TABLE IF NOT EXISTS MATCH_RESULT
(
    id INTEGER PRIMARY KEY,
    tournament_id INT,
    player_a TEXT NOT NULL,
    player_b TEXT NOT NULL,
    score_a INT,
    score_b INT,

    FOREIGN KEY (tournament_id)
    REFERENCES TOURNAMENT(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)