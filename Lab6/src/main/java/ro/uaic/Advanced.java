package ro.uaic;

import ro.uaic.movielist.MoviePartitioner;

/**
 * Advanced
 */
public class Advanced {
    public static void main(String[] args) {
        try {
            DBManager.setUseMigration(true);
            DBManager.loadMovies("src/main/resources/movies_metadata.csv", 200);
            DBManager.loadActors("src/main/resources/credits.csv", 1000);

            MoviePartitioner.createPartitionedLists();
        } catch (Exception e) {
            System.err.println("Error: " + e);
        } finally {
            DBManager.close();
        }
    }
}
