package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.repository.filmsession.FilmSessionRepository;
import ru.job4j.cinema.repository.filmsession.Sql2oFilmSessionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmSessionRepositoryTest {

    private static FilmSessionRepository sessionRepository;

    private static Sql2o sql2o;

    private static List<FilmSession> sessionList;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var dataSource = configuration.dataSource(url, username, password);
        sql2o = configuration.sql2o(dataSource);

        sessionRepository = new Sql2oFilmSessionRepository(sql2o);

        sessionList = List.of(
                new FilmSession(0, 1, 1, now(), now(), 1),
                new FilmSession(0, 1, 1, now().plusHours(5), now().plusHours(5), 2)
        );

        try (var connection = sql2o.open()) {
            connection.createQuery("SET REFERENTIAL_INTEGRITY = FALSE").executeUpdate();

            var query = connection.createQuery(
                    """
                            INSERT INTO film_sessions (film_id, halls_id, start_time, end_time, price)
                            VALUES (:filmId, :hallsId, :startTime, :endTime, :price)
                            """,
                    true
            );

            for (var session : sessionList) {
                query.addParameter("filmId", session.getFilmId())
                        .addParameter("hallsId", session.getHallsId())
                        .addParameter("startTime", session.getStartTime())
                        .addParameter("endTime", session.getEndTime())
                        .addParameter("price", session.getPrice())
                        .addToBatch();
            }

            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < sessionList.size(); i++) {
                sessionList.get(i).setId((Integer) generatedKeys[i]);
            }
        }
    }

    @AfterAll
    public static void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM film_sessions").executeUpdate();
            connection.createQuery("SET REFERENTIAL_INTEGRITY = TRUE").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnSession() {
        var expectedSession = sessionList.get(0);

        var actualSession = sessionRepository.findById(sessionList.get(0).getId());

        assertThat(actualSession).isPresent().get()
                .usingComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.SECONDS)),
                        LocalDateTime.class
                )
                .usingRecursiveComparison()
                .isEqualTo(expectedSession);
    }

    @Test
    public void whenFindNonExistentWhenReturnOptionalEmpty() {

        var genre = sessionRepository.findById(-1);

        assertThat(genre).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnList() {

        var genres = sessionRepository.findAll();

        assertThat(genres)
                .usingComparatorForType(
                        (Comparator.comparing(o -> o.truncatedTo(ChronoUnit.DAYS))),
                        LocalDateTime.class
                )
                .usingRecursiveComparison()
                .isEqualTo(sessionList);
    }

}
