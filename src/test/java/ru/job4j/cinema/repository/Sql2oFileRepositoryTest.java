package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oFileRepositoryTest {

    private static FileRepository fileRepository;

    private static Sql2o sql2o;

    private static List<File> fileList;

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

        fileRepository = new Sql2oFileRepository(sql2o);

        fileList = List.of(
                new File(0, "name1", "path1"),
                new File(0, "name2", "path2")
        );

        try (var connection = sql2o.open()) {
            var query = connection.createQuery("INSERT INTO files (name, path) VALUES (:name, :path)", true);
            for (var file : fileList) {
                query.addParameter("name", file.getName())
                        .addParameter("path", file.getPath())
                        .addToBatch();
            }
            var generatedKeys = query.executeBatch().getKeys();
            for (int i = 0; i < fileList.size(); i++) {
                fileList.get(i).setId((Integer) generatedKeys[i]);
            }
        }
    }

    @AfterAll
    public static void destroyRepositories() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM files").executeUpdate();
        }
    }

    @Test
    public void whenFindByIdThenReturnFile() {
        var expectedFile = fileList.get(1);

        var actualFile = fileRepository.findById(fileList.get(1).getId());

        assertThat(actualFile).isPresent().get()
                .isEqualTo(expectedFile);
    }

    @Test
    public void whenFindNonExistentIdThenReturnOptionalEmpty() {

        var actualFile = fileRepository.findById(123);

        assertThat(actualFile).isEmpty();
    }

}
