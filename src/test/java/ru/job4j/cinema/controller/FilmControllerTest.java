package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmController filmController;

    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenGetListThenReturnList() {
        var filmDTO1 = new FilmDTO();
        var filmDTO2 = new FilmDTO();
        var expectedList = List.of(filmDTO1, filmDTO2);
        when(filmService.findAll()).thenReturn(expectedList);

        var model = new ConcurrentModel();
        var view = filmController.getListPage(model);
        var actualList = model.getAttribute("films");

        assertThat(view).isEqualTo("/films/list");
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    public void whenFindFilmByIdThenReturnFilm() {
        var filmDTO = FilmDTO.builder()
                .id(1)
                .build();
        when(filmService.findById(1)).thenReturn(Optional.of(filmDTO));

        var model = new ConcurrentModel();
        var view = filmController.getFilmPage(1, model);
        var actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("/films/_id");
        assertThat(actualFilm).isEqualTo(filmDTO);
    }

    @Test
    public void whenFindNonExistentFilmThenRedirectList() {
        when(filmService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = filmController.getFilmPage(1, model);

        assertThat(view).isEqualTo("redirect:/films/list");
    }

}
