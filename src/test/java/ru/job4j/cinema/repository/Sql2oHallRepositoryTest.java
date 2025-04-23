package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oHallRepositoryTest {

    private static HallRepository hallRepository;

    private static Sql2o sql2o;

    private static List<Hall> hallList;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (InputStream inputStream = Sql2oGenreRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }

        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var dataSource = configuration.dataSource(url, username, password);
        sql2o = configuration.sql2o(dataSource);

        hallRepository = new Sql2oHallRepository(sql2o);

        hallList = List.of(
                new Hall(0, "hall1", 1, 1, "desc1"),
                new Hall(0, "hall2", 2, 2, "desc2"),
                new Hall(0, "hall3", 3, 3, "desc3")
        );

        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                            INSERT INTO halls (name, row_count, place_count, description)
                            VALUES (:name, :rowCount, :placeCount, :description)
                            """,
                    true
            );
            for (var hall : hallList) {
                query.addParameter("name", hall.getName())
                        .addParameter("rowCount", hall.getRowCount())
                        .addParameter("placeCount", hall.getPlaceCount())
                        .addParameter("description", hall.getDescription())
                        .addToBatch();
            }
            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < hallList.size(); i++) {
                hallList.get(i).setId((Integer) generatedKeys[i]);
            }
        }

    }

    @AfterAll
    public static void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM halls").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnHall() {
        var expectedHall = hallList.get(0);

        var actualHall = hallRepository.findById(hallList.get(0).getId());

        assertThat(actualHall).isPresent().get()
                .isEqualTo(expectedHall);
    }

    @Test
    public void whenFindNonExistentWhenReturnOptionalEmpty() {

        var hall = hallRepository.findById(-1);

        assertThat(hall).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnList() {

        var halls = hallRepository.findAll();

        assertThat(halls).hasSameElementsAs(hallList);
    }

}
