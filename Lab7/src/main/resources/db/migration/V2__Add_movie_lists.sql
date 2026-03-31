CREATE TABLE movie_lists (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movie_list_map (
    list_id INTEGER REFERENCES movie_lists(id) ON DELETE CASCADE,
    movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    PRIMARY KEY (list_id, movie_id)
);
