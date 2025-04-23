package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFilmSessionServiceTest {

    private FilmSessionService filmSessionService;

    private FilmSessionRepository filmSessionRepository;

    private FilmService filmService;

    private HallService hallService;

    @BeforeEach
    public void setup() {
        filmSessionRepository = mock(FilmSessionRepository.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        filmSessionService = new SimpleFilmSessionService(filmSessionRepository, filmService, hallService);
    }

    @Test
    public void whenFindByIdThenReturnFilmSessionDTO() {
        var hall = new Hall(1, "hall1", 2, 2, "hall2");
        var filmDTO = new FilmDTO(1, "test", "test", 2025, "test", 1, 1, 1);
        var filmSession = new FilmSession(1, 1, 1, now(), now(), 1);
        when(hallService.findById(filmSession.getId())).thenReturn(Optional.of(hall));
        when(filmService.findById(filmSession.getFilmId())).thenReturn(Optional.of(filmDTO));
        when(filmSessionRepository.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));

        var filmSessionDTO = filmSessionService.findById(filmSession.getId());

        assertThat(filmSessionDTO).isPresent().get()
                .usingComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.MINUTES)),
                        LocalDateTime.class
                )
                .extracting("id", "film", "hall", "startTime", "endTime", "price")
                .containsExactly(filmSession.getId(), filmDTO, hall,
                        filmSession.getStartTime(), filmSession.getEndTime(), filmSession.getPrice());
    }

    @Test
    public void whenFindAllThenReturnFilmSessionDTOs() {
        var hall = new Hall(1, "hall1", 2, 2, "hall2");
        var filmDTO = new FilmDTO(1, "test", "test", 2025, "test", 1, 1, 1);
        var filmSession1 = new FilmSession(1, 1, 1, now(), now(), 1);
        var filmSession2 = new FilmSession(2, 1, 1, now().plusHours(4), now().plusHours(4), 1);
        var expectedSessions = List.of(filmSession1, filmSession2);
        when(hallService.findById(any(Integer.class))).thenReturn(Optional.of(hall));
        when(filmService.findById(any(Integer.class))).thenReturn(Optional.of(filmDTO));
        when(filmSessionRepository.findAll()).thenReturn(List.of(filmSession1, filmSession2));

        var filmSessionDTOs = filmSessionService.findAll();

        assertThat(filmSessionDTOs)
                .usingComparatorForType(
                        Comparator.comparing(o -> o.truncatedTo(ChronoUnit.MINUTES)),
                        LocalDateTime.class
                )
                .usingRecursiveComparison()
                .ignoringFields("film", "hall", "hallsId")
                .isEqualTo(expectedSessions);
    }

}
