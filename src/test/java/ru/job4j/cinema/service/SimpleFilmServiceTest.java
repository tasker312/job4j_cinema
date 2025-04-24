package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.genre.GenreRepository;
import ru.job4j.cinema.service.film.FilmService;
import ru.job4j.cinema.service.film.SimpleFilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmServiceTest {

    private FilmService filmService;

    private FilmRepository filmRepository;

    private GenreRepository genreRepository;

    @BeforeEach
    public void setup() {
        filmRepository = mock(FilmRepository.class);
        genreRepository = mock(GenreRepository.class);
        filmService = new SimpleFilmService(filmRepository, genreRepository);
    }

    @Test
    public void whenFindFilmWhenReturnFilmDTO() {
        var genre = new Genre(1, "genre");
        var film = new Film(1, "film1", "desc1", 1, 1, 1, 1, 1);
        when(filmRepository.findById(1)).thenReturn(Optional.of(film));
        var idCaptor = ArgumentCaptor.forClass(Integer.class);
        when(genreRepository.findById(idCaptor.capture())).thenReturn(Optional.of(genre));

        var filmDTO = filmService.findById(1);

        assertThat(filmDTO).isPresent().get()
                .usingRecursiveComparison()
                .ignoringFields("genre", "genreId")
                .isEqualTo(film);
        assertThat(idCaptor.getValue()).isEqualTo(genre.getId());

    }

    @Test
    public void whenFindNonExistentFilmWhenReturnOptionalEmpty() {
        when(filmRepository.findById(1)).thenReturn(Optional.empty());

        var film = filmService.findById(1);

        assertThat(film).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnAllFilmDTOs() {
        var genre1 = new Genre(1, "genre1");
        var genre2 = new Genre(2, "genre2");
        when(genreRepository.findAll()).thenReturn(List.of(genre1, genre2));
        var film1 = new Film(1, "film1", "desc1", 1, 1, 1, 1, 1);
        var film2 = new Film(2, "film1", "desc1", 2, 2, 2, 2, 2);
        var expectedFilms = List.of(film1, film2);
        when(filmRepository.findAll()).thenReturn(expectedFilms);

        var filmDTOs = filmService.findAll();

        assertThat(filmDTOs)
                .usingRecursiveComparison()
                .ignoringFields("genre", "genreId")
                .isEqualTo(expectedFilms);
    }

}
