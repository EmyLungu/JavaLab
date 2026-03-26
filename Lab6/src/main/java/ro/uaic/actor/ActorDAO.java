package ro.uaic.actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ro.uaic.DBManager;

/**
 * ActorDAO
 */
public class ActorDAO {
    public void create(Actor actor) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO actors (first_name, last_name) VALUES (?, ?)"
        );
        pstmt.setString(1, actor.getFirstName());
        pstmt.setString(2, actor.getLastName());
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    public Actor findByName(String firstName, String lastName) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT id FROM actors WHERE first_name = ? AND last_name = ?"
        );
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        ResultSet rs = pstmt.executeQuery();

        Actor result = null;
        if (rs.next()) {
            result = new Actor (
                rs.getInt(1),
                firstName,
                lastName
            );
        }

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }

    public Actor findById(int id) throws SQLException {
        Connection conn = DBManager.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT first_name, last_name FROM actors WHERE id = ?"
        );
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        Actor result = null;
        if (rs.next()) {
            result = new Actor (
                id,
                rs.getString(1),
                rs.getString(2)
            );
        }

        rs.close();
        pstmt.close();
        conn.close();

        return result;
    }

    public void createCast(int actorId, int movieId) throws SQLException {
        try (Connection conn = DBManager.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO movie_cast (id_actor, id_movie) VALUES (?, ?)" +
                    "ON CONFLICT DO NOTHING"
                    );
            pstmt.setInt(1, actorId);
            pstmt.setInt(2, movieId);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        }
    }


    public List<Integer> getActorsForMovie(int movieId) throws SQLException {
        String sql = "SELECT id_actor FROM movie_cast WHERE id_movie = ?";
        List<Integer> actors = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                actors.add(rs.getInt("id_actor"));
            }
        }
        return actors;
    }   
}
