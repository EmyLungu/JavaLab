package ro.uaic.movielist;

import ro.uaic.movie.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.uaic.DBManager;
import ro.uaic.actor.ActorDAO;
import ro.uaic.movielist.MovieList;
import ro.uaic.movielist.MovieListDAO;

/**
 * MoviePartinioner
 */
public class MoviePartitioner {
    public static boolean areRelated(int movieId1, int movieId2) throws Exception {
        ActorDAO actorDAO = new ActorDAO();
        List<Integer> actors1 = actorDAO.getActorsForMovie(movieId1);
        List<Integer> actors2 = actorDAO.getActorsForMovie(movieId2);
        
        for (int actorId : actors1) {
            if (actors2.contains(actorId)) {
                return true;
            }
        }
        return false;
    }
    
    public static Map<Integer, List<Integer>> buildGraph(List<Movie> movies) throws Exception {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        for (int i = 0; i < movies.size(); i++) {
            int id1 = movies.get(i).getId();
            graph.putIfAbsent(id1, new ArrayList<>());
            
            for (int j = i + 1; j < movies.size(); j++) {
                int id2 = movies.get(j).getId();
                if (areRelated(id1, id2)) {
                    graph.get(id1).add(id2);
                    graph.putIfAbsent(id2, new ArrayList<>());
                    graph.get(id2).add(id1);
                }
            }
        }
        
        return graph;
    }
    
    public static List<List<Movie>> partitionMovies(List<Movie> movies, Map<Integer, List<Integer>> graph) {
        List<List<Movie>> lists = new ArrayList<>();
        Map<Integer, Integer> movieToColor = new HashMap<>();
        
        List<Movie> sortedMovies = new ArrayList<>(movies);
        sortedMovies.sort((m1, m2) -> 
            graph.getOrDefault(m2.getId(), new ArrayList<>()).size() - 
            graph.getOrDefault(m1.getId(), new ArrayList<>()).size()
        );
        
        for (Movie movie : sortedMovies) {
            int movieId = movie.getId();
            Set<Integer> usedColors = new HashSet<>();
            
            for (int relatedId : graph.getOrDefault(movieId, new ArrayList<>())) {
                if (movieToColor.containsKey(relatedId)) {
                    usedColors.add(movieToColor.get(relatedId));
                }
            }
            
            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            
            movieToColor.put(movieId, color);

            while (lists.size() <= color) {
                lists.add(new ArrayList<>());
            }
            lists.get(color).add(movie);
        }
        
        return lists;
    }
    
    public static void balanceLists(List<List<Movie>> lists) {
        if (lists.isEmpty())
            return;
        
        int totalMovies = lists.stream().mapToInt(List::size).sum();
        int targetSize = (int) Math.ceil((double) totalMovies / lists.size());
        
        boolean changed;
        do {
            changed = false;
            lists.sort((a, b) -> b.size() - a.size());
            
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).size() <= targetSize) break;
                
                for (int j = lists.size() - 1; j > i; j--) {
                    if (lists.get(j).size() < targetSize) {
                        Movie movie = lists.get(i).remove(lists.get(i).size() - 1);
                        lists.get(j).add(movie);
                        changed = true;
                        break;
                    }
                }
            }
        } while (changed);
    }
    
    public static void createPartitionedLists() throws Exception {
        List<Movie> movies = DBManager.getMovies();

        if (movies.isEmpty()) return;
        
        Map<Integer, List<Integer>> graph = buildGraph(movies);
        List<List<Movie>> partitions = partitionMovies(movies, graph);
        // balanceLists(partitions);
        
        MovieListDAO listDAO = new MovieListDAO();
        for (int i = 0; i < partitions.size(); i++) {
            String listName = "Movie List " + (i + 1);
            MovieList movieList = new MovieList(listName);
            listDAO.create(movieList);
            
            for (Movie movie : partitions.get(i)) {
                listDAO.addMovieToList(movieList.getId(), movie.getId());
            }
            
            System.out.println("Created " + listName + " with " + 
                             partitions.get(i).size() + " movies");
        }
        
        System.out.println("List sizes: " +
            partitions.stream()
            .mapToInt(List::size)
            .boxed()
            .toList()
        );
    }
}
