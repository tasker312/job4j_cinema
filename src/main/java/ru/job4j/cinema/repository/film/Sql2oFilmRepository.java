package ru.job4j.cinema.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    @Override
    public Optional<Film> findById(int id) {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films WHERE id = :id")
                    .addParameter("id", id);
            var film = query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetchFirst(Film.class);
            return Optional.ofNullable(film);
        }
    }

    @Override
    public Collection<Film> findAll() {
        try (Connection connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM films");
            return query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
        }
    }

}
