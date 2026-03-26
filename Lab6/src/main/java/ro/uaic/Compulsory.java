package ro.uaic;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import ro.uaic.genre.Genre;
import ro.uaic.genre.GenreDAO;

/**
 * Compulsory
 */
public class Compulsory {
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
                "title TEXT, " +
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
        };
        
        for (String table : tables) {
            stmt.executeUpdate(table);
        }


        conn.close();
    }

    public static void main(String[] args) {
        try {
            createTables();

            GenreDAO genres = new GenreDAO();
            Genre action = new Genre("Action");
            Genre medieval = new Genre("Medieval");
            Genre documentary = new Genre("Documentary");
            genres.create(action);
            genres.create(medieval);
            genres.create(documentary);

            Genre g1 = genres.findByName("Documentary");
            System.out.println("Found Documentary with ID: " + g1.getId());
            Genre g2 = genres.findById(g1.getId());
            System.out.println("Found Genre Name for ID " + g1.getId() + ": " + g2.getId());

        } catch (Exception e) {
            System.err.println("Error: " + e);
        } finally {
            DBManager.close();
        }
    }
}
