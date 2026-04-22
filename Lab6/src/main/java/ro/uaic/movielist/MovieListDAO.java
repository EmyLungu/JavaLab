package ro.uaic.movielist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ro.uaic.DBManager;
import ro.uaic.movie.Movie;

/**
 * MovieListDAO
 */

public class MovieListDAO {
    
    public void create(MovieList list) throws SQLException {
        String sql = "INSERT INTO movie_lists (name) VALUES (?) RETURNING id";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, list.getName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                list.setId(rs.getInt(1));
            }
        }
    }
    
    public void addMovieToList(int listId, int movieId) throws SQLException {
        String sql = "INSERT INTO movie_list_map (list_id, movie_id) VALUES (?, ?) " +
                     "ON CONFLICT DO NOTHING";
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, listId);
            pstmt.setInt(2, movieId);
            pstmt.executeUpdate();
        }
    }
    
    public MovieList findById(int id) throws SQLException {
        String sql = "SELECT * FROM movie_lists WHERE id = ?";
        MovieList list = null;
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                list = new MovieList();
                list.setId(rs.getInt("id"));
                list.setName(rs.getString("name"));
                list.setCreatedAt(rs.getTimestamp("created_at"));
                
                list.setMovies(getMoviesForList(id));
            }
        }
        return list;
    }
    
    private List<Movie> getMoviesForList(int listId) throws SQLException {
        String sql = "SELECT m.* FROM movies m " +
                     "JOIN movie_list_map mlm ON m.id = mlm.movie_id " +
                     "WHERE mlm.list_id = ?";
        List<Movie> movies = new ArrayList<>();
        
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, listId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDate("release_date"),
                    rs.getInt("duration"),
                    rs.getInt("score"),
                    rs.getInt("genre_id")
                );
                movies.add(movie);
            }
        }
        return movies;
    }
}
