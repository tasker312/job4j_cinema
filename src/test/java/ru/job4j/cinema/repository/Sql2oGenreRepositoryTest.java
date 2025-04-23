package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oGenreRepositoryTest {

    private static GenreRepository genreRepository;

    private static Sql2o sql2o;

    private static List<Genre> genreList;

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

        genreRepository = new Sql2oGenreRepository(sql2o);

        genreList = List.of(
                new Genre(1, "Genre1"),
                new Genre(2, "Genre2"),
                new Genre(2, "Genre3")
        );

        try (var connection = sql2o.open()) {
            var query = connection.createQuery("INSERT INTO genres (name) VALUES (:name)", true);
            for (var file : genreList) {
                query.addParameter("name", file.getName())
                        .addToBatch();
            }
            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < genreList.size(); i++) {
                genreList.get(i).setId((Integer) generatedKeys[i]);
            }
        }
    }

    @AfterAll
    public static void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM genres").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnGenre() {
        var expectedGenre = genreList.get(0);

        var actualGenre = genreRepository.findById(genreList.get(0).getId());

        assertThat(actualGenre).isPresent().get()
                .isEqualTo(expectedGenre);
    }

    @Test
    public void whenFindNonExistentWhenReturnOptionalEmpty() {

        var genre = genreRepository.findById(-1);

        assertThat(genre).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnList() {

        var genres = genreRepository.findAll();

        assertThat(genres).hasSameElementsAs(genreList);
    }

}
