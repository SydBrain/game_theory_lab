CREATE TABLE IF NOT EXISTS TOURNAMENT
(
    id INTEGER PRIMARY KEY,
    game_name TEXT NOT NULL,
    n_rounds INT CHECK(n_rounds > 0),
    happened_on TEXT NOT NULL
)