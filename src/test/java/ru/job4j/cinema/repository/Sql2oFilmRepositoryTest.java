package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFilmRepositoryTest {

    private static FilmRepository filmRepository;

    private static Sql2o sql2o;

    private static List<Film> filmList;

    @BeforeAll
    static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var input = Sql2oFileRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(input);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var dataSource = configuration.dataSource(url, username, password);
        sql2o = configuration.sql2o(dataSource);

        filmRepository = new Sql2oFilmRepository(sql2o);

        filmList = List.of(
                new Film(0, "name1", "desc1", 1, 0, 1, 1, 0),
                new Film(0, "name2", "desc2", 2, 0, 2, 2, 0),
                new Film(0, "name3", "desc3", 3, 0, 3, 3, 0)
        );

        try (var connection = sql2o.open()) {
            connection.createQuery("SET REFERENTIAL_INTEGRITY = FALSE").executeUpdate();

            var query = connection.createQuery(
                    """
                            INSERT INTO films (name, description, year, genre_id, minimal_age, duration_in_minutes, file_id)
                            VALUES (:name, :description, :year, :genreId, :minimalAge, :durationInMinutes, :fileId)
                            """,
                    true
            );
            for (var film : filmList) {
                query.addParameter("name", film.getName())
                        .addParameter("description", film.getDescription())
                        .addParameter("year", film.getYear())
                        .addParameter("genreId", film.getGenreId())
                        .addParameter("minimalAge", film.getMinimalAge())
                        .addParameter("durationInMinutes", film.getDurationInMinutes())
                        .addParameter("fileId", film.getFileId())
                        .addToBatch();
            }
            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < filmList.size(); i++) {
                filmList.get(i).setId((Integer) generatedKeys[i]);
            }
        }
    }

    @AfterAll
    public static void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM films").executeUpdate();
            connection.createQuery("SET REFERENTIAL_INTEGRITY = TRUE").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnFilm() {
        var expectedFilm = filmList.get(1);

        var actualFilm = filmRepository.findById(filmList.get(1).getId());

        assertThat(actualFilm).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(expectedFilm);
    }

    @Test
    public void whenFindNonExistentIdThenReturnOptionalEmpty() {

        var actualFilm = filmRepository.findById(-1);

        assertThat(actualFilm).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnList() {

        var actualFilms = filmRepository.findAll();

        assertThat(actualFilms)
                .hasSameElementsAs(filmList);
    }

}
