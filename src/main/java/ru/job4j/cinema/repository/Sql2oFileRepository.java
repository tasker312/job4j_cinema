package ru.job4j.cinema.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    @Override
    public Optional<File> findById(int id) {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM files WHERE id = :id")
                    .addParameter("id", id);
            var file = query.executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }

}
