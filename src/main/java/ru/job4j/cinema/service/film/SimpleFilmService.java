package ru.job4j.cinema.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.genre.GenreRepository;
import ru.job4j.cinema.util.converter.FilmConverter;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    @Override
    public Optional<FilmDTO> findById(int id) {
        var film = filmRepository.findById(id);
        if (film.isEmpty()) {
            return Optional.empty();
        }
        var genre = genreRepository.findById(film.get().getGenreId())
                .map(Genre::getName)
                .orElse("");
        var filmDTO = FilmConverter.toFilmDTO(film.get(), genre);
        return Optional.of(filmDTO);
    }

    @Override
    public Collection<FilmDTO> findAll() {
        var films = filmRepository.findAll();
        var genres = genreRepository.findAll();
        return films.stream()
                .map(film -> {
                    var genre = genres.stream()
                            .filter(g -> g.getId() == film.getGenreId())
                            .findFirst()
                            .map(Genre::getName)
                            .orElse("");
                    return FilmConverter.toFilmDTO(film, genre);
                }).toList();
    }

}
