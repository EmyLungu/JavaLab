DROP TABLE IF EXISTS movie_cast CASCADE;
DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS actors CASCADE;

CREATE TABLE actors (
    id SERIAL PRIMARY KEY,
    first_name TEXT,
    last_name TEXT
);

CREATE TABLE genres (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE
);

CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title TEXT UNIQUE,
    release_date DATE, duration INTEGER,
    score INTEGER,
    genre_id INTEGER REFERENCES genres(id) ON DELETE SET NULL
);

CREATE TABLE movie_cast (
    id_actor INTEGER REFERENCES actors(id) ON DELETE CASCADE,
    id_movie INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    PRIMARY KEY (id_actor, id_movie)
);

CREATE OR REPLACE VIEW movie_report_view AS
    SELECT m.id, m.title, m.release_date, m.duration, m.score, m.genre_id, g.name AS genre_name
    FROM movies m
    LEFT JOIN genres g ON m.genre_id = g.id;
