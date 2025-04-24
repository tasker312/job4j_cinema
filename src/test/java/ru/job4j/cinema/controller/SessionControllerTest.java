package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDTO;
import ru.job4j.cinema.dto.HallDTO;
import ru.job4j.cinema.service.filmsession.FilmSessionService;
import ru.job4j.cinema.service.hall.HallService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionControllerTest {

    private SessionController sessionController;

    private FilmSessionService filmSessionService;

    private HallService hallService;

    @BeforeEach
    public void setUp() {
        filmSessionService = mock(FilmSessionService.class);
        hallService = mock(HallService.class);
        sessionController = new SessionController(filmSessionService, hallService);
    }

    @Test
    public void whenGetListThenReturnList() {
        var expectedList = List.of(new FilmSessionDTO(), new FilmSessionDTO());
        when(filmSessionService.findAll()).thenReturn(expectedList);

        var model = new ConcurrentModel();
        var view = sessionController.getSessionsList(model);
        var actualList = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("/sessions/list");
        assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    public void whenFindFilmSessionByIdThenReturnFilmSession() {
        var filmSessionDTO = FilmSessionDTO.builder().id(1).build();
        var hallDTO = new HallDTO();
        when(filmSessionService.findById(1)).thenReturn(Optional.of(filmSessionDTO));
        when(hallService.findByFilmSession(filmSessionDTO.getId())).thenReturn(Optional.of(hallDTO));

        var model = new ConcurrentModel();
        var view = sessionController.getSessionPage(1, model);
        var actualSession = model.getAttribute("filmSession");
        var actualHall = model.getAttribute("hall");

        assertThat(view).isEqualTo("/sessions/_id");
        assertThat(actualSession)
                .usingRecursiveComparison()
                .isEqualTo(filmSessionDTO);
        assertThat(actualHall)
                .usingRecursiveComparison()
                .isEqualTo(hallDTO);
    }

    @Test
    public void whenFilmSessionNonExistentFilmThenRedirectList() {
        when(filmSessionService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = sessionController.getSessionPage(1, model);

        assertThat(view).isEqualTo("redirect:/sessions");
    }

}
