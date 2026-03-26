package ro.uaic.genre;

import ro.uaic.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * GenreDAO
 */
public class GenreDAO {
    public void create(Genre genre) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO genres (name) VALUES (?) " +
            "ON CONFLICT (name) DO NOTHING;"
        );
        pstmt.setString(1, genre.getName());
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public Genre findByName(String name) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM genres WHERE name = ?");
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        Integer id = (rs.next() ? rs.getInt(1) : null);
        Genre result = new Genre(id, name);

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }

    public Genre findById(int id) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM genres WHERE id = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        String name = (rs.next() ? rs.getString(1) : null);
        Genre result = new Genre(id, name);

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }
}
