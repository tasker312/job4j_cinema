package ru.job4j.cinema.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.User;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    @Override
    public Optional<User> save(User user) {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery(
                            "INSERT INTO users (full_name, email, password) VALUES (:fullName, :email, :password)",
                            true
                    )
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int userId = query.executeUpdate().getKey(Integer.class);
            user.setId(userId);
            return Optional.of(user);
        } catch (Sql2oException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
                    .addParameter("email", email)
                    .addParameter("password", password);
            var result = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(result);
        }
    }

}
