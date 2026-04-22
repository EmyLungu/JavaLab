package ro.uaic.movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ro.uaic.DBManager;

/**
 * MovieDAO
 */
public class MovieDAO {
    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score, genre_id)" +
                     "VALUES (?, ?, ?, ?, ?)" +
                     "ON CONFLICT (title) DO NOTHING;";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, movie.getTitle());
            pstmt.setDate(2, movie.getReleaseDate());
            pstmt.setInt(3, movie.getDuration());
            pstmt.setInt(4, movie.getScore());
            pstmt.setInt(5, movie.getGenreId());
            pstmt.executeUpdate();
        }
    }

    public Movie findByTitle(String title) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT id, release_date, duration, score, genre_id FROM movies WHERE title = ?"
        );
        pstmt.setString(1, title);
        ResultSet rs = pstmt.executeQuery();

        Movie result = null;
        if (rs.next()) {
            result = new Movie(
                rs.getInt(1),
                title,
                rs.getDate(2),
                rs.getInt(3),
                rs.getInt(4),
                rs.getInt(5)
            );
        }

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }

    public Movie findById(int id) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT title, release_date, duration, score, genreId FROM movies WHERE id = ?"
        );
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        Movie result = null;
        if (rs.next()) {
            result = new Movie(
                id,
                rs.getString(1),
                rs.getDate(2),
                rs.getInt(3),
                rs.getInt(4),
                rs.getInt(5)
            );
        }

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }
}
