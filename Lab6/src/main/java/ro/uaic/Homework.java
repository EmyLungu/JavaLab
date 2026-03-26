package ro.uaic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.SQLException;

import ro.uaic.genre.Genre;
import ro.uaic.genre.GenreDAO;
import ro.uaic.movie.Movie;
import ro.uaic.movie.MovieDAO;

/**
 * Homework 
 */
public class Homework {
    public static void createTables() throws SQLException {
        Connection conn = DBManager.getConnection();

        Statement stmt = conn.createStatement();
        String[] tables = {
            "CREATE TABLE IF NOT EXISTS actors (" +
                "id SERIAL PRIMARY KEY, " +
                "first_name TEXT, " +
                "last_name TEXT" +
            ")",

            "CREATE TABLE IF NOT EXISTS genres (" +
                "id SERIAL PRIMARY KEY, " +
                "name TEXT UNIQUE" +
            ")",

            "CREATE TABLE IF NOT EXISTS movies (" +
                "id SERIAL PRIMARY KEY, " +
                "title TEXT UNIQUE, " +
                "release_date DATE, " +
                "duration INTEGER, " +
                "score INTEGER, " +
                "genre_id INTEGER, " +
                "CONSTRAINT fk_genre FOREIGN KEY(genre_id) REFERENCES genres(id) ON DELETE SET NULL" +
            ")",

            "CREATE TABLE IF NOT EXISTS movie_cast (" +
                "id_actor INTEGER REFERENCES actors(id) ON DELETE CASCADE, " +
                "id_movie INTEGER REFERENCES movies(id) ON DELETE CASCADE, " +
                "id SERIAL PRIMARY KEY" +
            ")",

            "CREATE OR REPLACE VIEW movie_report_view AS " +
                "SELECT m.id, m.title, m.release_date, m.duration, m.score, m.genre_id, g.name AS genre_name " +
                "FROM movies m " +
                "LEFT JOIN genres g ON m.genre_id = g.id"
        };
        
        for (String table : tables) {
            stmt.executeUpdate(table);
        }

        conn.close();
    }

    public static void addGenres() throws SQLException {
        GenreDAO genres = new GenreDAO();
        Genre action = new Genre("Action");
        Genre medieval = new Genre("Medieval");
        Genre documentary = new Genre("Documentary");
        genres.create(action);
        genres.create(medieval);
        genres.create(documentary);
    }

    public static void addMovies() throws SQLException {
        GenreDAO genres = new GenreDAO();
        Genre documentary = genres.findByName("Documentary");
        Genre action = genres.findByName("Action");
        Genre medieval = genres.findByName("Medieval");

        MovieDAO movies = new MovieDAO();
        Movie m1 = new Movie("Old movie", Date.valueOf("2001-10-9"), 137, 8, medieval.getId());
        Movie m2 = new Movie("Terminator", Date.valueOf("2009-11-2"), 155, 9, action.getId());
        Movie m3 = new Movie(
            "Pantera Documentary", Date.valueOf("2018-6-18"), 71, 7, documentary.getId()
        );

        movies.create(m1);
        movies.create(m2);
        movies.create(m3);
    }

    public static void main(String[] args) {
        try {
            createTables();
            addGenres();
            addMovies();
            DBManager.getReport();

        } catch (Exception e) {
            System.err.println("Error: " + e);
        } finally {
            DBManager.close();
        }
    }
}
