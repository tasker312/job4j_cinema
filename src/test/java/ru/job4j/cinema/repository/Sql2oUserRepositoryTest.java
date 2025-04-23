package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import java.io.InputStream;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {

    private static UserRepository userRepository;

    protected static Sql2o sql2o;

    @BeforeAll
    public static void init() throws Exception {
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

        userRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void cleanUp() {
        try (var connection = sql2o.open()) {
            connection.createQuery("DELETE FROM users").executeUpdate();
        }
    }

    @Test
    public void whenSaveUserThenSameFromRepository() {
        var user = new User(0, "ivan", "ivan@test", "pass");

        var savedUser = userRepository.save(user);
        user.setId(savedUser.get().getId());

        assertThat(savedUser).isPresent().get()
                .isEqualTo(user);
    }

    @Test
    public void whenSaveManyUsersWithDifferentEmailsThenSameFromRepository() {
        var user1 = new User(0, "ivan1", "ivan@test1", "pass1");
        var user2 = new User(0, "ivan2", "ivan@test2", "pass2");

        var savedUser1 = userRepository.save(user1);
        user1.setId(savedUser1.get().getId());
        var savedUser2 = userRepository.save(user2);
        user2.setId(savedUser2.get().getId());

        assertThat(savedUser1).isPresent().get()
                .isEqualTo(user1);

        assertThat(savedUser2).isPresent().get()
                .isEqualTo(user2);
    }

    @Test
    public void whenSaveManyUsersWithSameEmailsThenOptionalEmpty() {
        var user1 = new User(0, "ivan1", "ivan@test", "pass1");
        var user2 = new User(0, "ivan2", "ivan@test", "pass2");

        var savedUser1 = userRepository.save(user1);
        user1.setId(savedUser1.get().getId());
        var savedUser2 = userRepository.save(user2);

        assertThat(savedUser1).isPresent().get()
                .isEqualTo(user1);

        assertThat(savedUser2).isEmpty();
    }

    @Test
    public void whenFindByEmailAndPasswordThenReturnUser() {
        var user = userRepository.save(new User(0, "ivan", "ivan@test", "pass"));
        var foundUser = userRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());

        assertThat(foundUser).isPresent().get()
                .isEqualTo(user.get());
    }

    @Test
    public void whenFindByNonExistentEmailAndPasswordThenReturnOptionalEmpty() {
        assertThat(userRepository.findByEmailAndPassword("", null)).isEmpty();
    }

}
