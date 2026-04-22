package ro.uaic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ro.uaic.movie.MovieDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class Client {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "http://localhost:8080/movies";

    public Client() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public List<MovieDTO> getMovies() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<List<MovieDTO>>() {});
    }

    public MovieDTO createMovie(MovieDTO movie) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(movie);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), MovieDTO.class);
    }

    public MovieDTO updateMovie(int id, MovieDTO movie) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(movie);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), MovieDTO.class);
    }

    public void patchScore(int id, int newScore) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id + "/score?score=" + newScore))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public void deleteMovie(int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .DELETE()
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.discarding());
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        
        System.out.println("Fetching all movies...");
        List<MovieDTO> movies = client.getMovies();

        movies.forEach(m -> System.out.println(m.title()));
        MovieDTO lastMovie = movies.get(movies.size() - 1);

        System.out.println("Last movie title: " + lastMovie.title());
        System.out.println("Last movie score: " + lastMovie.score());

        System.out.println("Updating score for last movie score with 3");
        client.patchScore(lastMovie.id(), 3);

        movies = client.getMovies();
        lastMovie = movies.get(movies.size() - 1);
        System.out.println("Last movie score: " + lastMovie.score());
        
        System.out.println("Deleting last movie...");
        client.deleteMovie(lastMovie.id());
    }
}
