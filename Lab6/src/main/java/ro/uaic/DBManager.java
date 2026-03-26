package ro.uaic;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flywaydb.core.Flyway;

import com.opencsv.CSVReader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import freemarker.template.Configuration;
import freemarker.template.Template;


import ro.uaic.actor.Actor;
import ro.uaic.actor.ActorDAO;
import ro.uaic.movie.Movie;
import ro.uaic.movie.MovieDAO;
import ro.uaic.genre.Genre;
import ro.uaic.genre.GenreDAO;

/**
 * DBManager
 */
public class DBManager {
    private static String jdbcUrl = "jdbc:postgresql://localhost/lab6db";
    private static HikariDataSource dataSource= null;
    private static boolean useMigration = false;
    private DBManager() {};

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername("admin");
            config.setPassword("admin");
            config.setMaximumPoolSize(32);
            config.setConnectionTimeout(30_000);

            dataSource = new HikariDataSource(config);

            if (useMigration) {
                Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .cleanDisabled(false)
                    .load();

                flyway.clean();
                flyway.migrate();
            }
        }

        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static List<Movie> getMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        Connection conn = DBManager.getConnection();

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM movie_report_view");
            
        while (rs.next()) {
            Movie m = new Movie(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getDate("release_date"),
                rs.getInt("duration"),
                rs.getInt("score"),
                rs.getInt("genre_id")
            );
            m.setGenreName(rs.getString("genre_name"));
            movies.add(m);
        }

        conn.close();
        return movies;
    }

    public static void getReport() throws Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);

        cfg.setDirectoryForTemplateLoading(new File("src/main/resources"));
        cfg.setDefaultEncoding("UTF-8");


        List<Movie> movies = getMovies();
        Map<String, Object> root = new HashMap<>();
        root.put("movies", movies);


        Template temp = cfg.getTemplate("report.ftl");
        File outputFile = new File("report.html");

        try (Writer out = new FileWriter(outputFile)) {
            temp.process(root, out);
        }

        Desktop.getDesktop().open(outputFile);
    }

    public static void setUseMigration(boolean useMigration) {
        DBManager.useMigration = useMigration;
    }

    public static void loadMovies(String filepath, int maxCount) throws SQLException {
        GenreDAO genres = new GenreDAO();
        MovieDAO movies = new MovieDAO();
        Map<String, Integer> genreMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            String[] row;
            reader.readNext();

            int count = 0;
            while((row = reader.readNext()) != null && count < maxCount) {
                if (row.length < 23) {
                    continue;
                }

                String title = row[20];
                String releaseStr = row[14];
                if (releaseStr == null || releaseStr.isEmpty())
                    continue;
                Date releaseDate = Date.valueOf(releaseStr);
                int duration = (int) Double.parseDouble(row[16].isEmpty() ? "0" : row[16]);
                int score = (int) Double.parseDouble(row[22].isEmpty() ? "0" : row[22]);

                String genreName = row[4];

                if (!genreMap.containsKey(genreName)) {
                    genres.create(new Genre(genreName));
                    Genre g = genres.findByName(genreName);
                    genreMap.put(genreName, g.getId());
                }

                Movie movie = new Movie(
                    title,
                    releaseDate,
                    duration,
                    score,
                    genreMap.get(genreName)
                );
                movie.setGenreName(genreName);

                movies.create(movie);
                count++;
            }
            reader.close();

            System.out.println("Movie Data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error: " +  e);
        }
    }

    public static void loadActors(String filepath, int maxCount) {
        ActorDAO actorDAO = new ActorDAO();


        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            String[] row;
            reader.readNext();

            List<Integer> movieIds = DBManager.getMovies()
                .stream()
                .mapToInt(s -> s.getId())
                .boxed()
                .collect(Collectors.toList());

            int count = 0;
            while((row = reader.readNext()) != null && count < maxCount) {
                if (row.length < 3) {
                    continue;
                }

                String castJson = row[0];
                int movieId = Integer.parseInt(row[2]);

                if (!movieIds.contains(movieId))
                    continue;

                int index = 0;
                while (true) {
                    int nameStart = castJson.indexOf("'name': '", index);
                    if (nameStart == -1) break;

                    nameStart += 9;
                    int nameEnd = castJson.indexOf("'", nameStart);
                    if (nameEnd == -1) break;

                    String fullName = castJson.substring(nameStart, nameEnd);

                    String firstName = fullName;
                    String lastName = "";
                    int spaceIndex = fullName.indexOf(' ');
                    if (spaceIndex > 0) {
                        firstName = fullName.substring(0, spaceIndex);
                        lastName = fullName.substring(spaceIndex + 1);
                    }

                    Actor actor = actorDAO.findByName(firstName, lastName);
                    if (actor == null) {
                        actor = new Actor(firstName, lastName);
                        actorDAO.create(actor);
                        actor = actorDAO.findByName(firstName, lastName);
                    }

                    if (actor != null) {
                        actorDAO.createCast(actor.getId(), movieId);
                    }

                    index = nameEnd;
                    count++;
                }
            }
            System.out.println("Actor Data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error parsing actors: " + e.getMessage());
        }
    }
}
