package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleHallServiceTest {

    private HallService hallService;

    private HallRepository hallRepository;

    private TicketRepository ticketRepository;

    private FilmSessionRepository filmSessionRepository;

    @BeforeEach
    public void setup() {
        hallRepository = mock(HallRepository.class);
        ticketRepository = mock(TicketRepository.class);
        filmSessionRepository = mock(FilmSessionRepository.class);
        hallService = new SimpleHallService(hallRepository, ticketRepository, filmSessionRepository);
    }

    @Test
    public void whenFindByIdThenReturnHall() {
        var hall = new Hall() {{
            setId(1);
        }};
        when(hallRepository.findById(1)).thenReturn(Optional.of(hall));

        var actualHall = hallService.findById(1);

        assertThat(actualHall).isPresent().get()
                .isEqualTo(hall);
    }

    @Test
    public void whenFindNonExistentHallThenReturnEmpty() {
        when(hallRepository.findById(1)).thenReturn(Optional.empty());

        var actualHall = hallService.findById(1);

        assertThat(actualHall).isEmpty();
    }

    @Test
    public void whenFindAllThenReturnHalls() {
        var halls = List.of(new Hall(), new Hall(), new Hall());
        when(hallRepository.findAll()).thenReturn(halls);

        var actualHalls = hallService.findAll();

        assertThat(actualHalls).hasSameElementsAs(halls);
    }

    @Test
    public void whenFindBySessionThenReturnHall() {
        var filmSession = new FilmSession(1, 1, 1, now(), now(), 1);
        when(filmSessionRepository.findById(filmSession.getId())).thenReturn(Optional.of(filmSession));
        var hall = new Hall(1, "test", 2, 2, "test");
        when(hallRepository.findById(hall.getId())).thenReturn(Optional.of(hall));
        var ticket1 = new Ticket(1, 1, 1, 1, 1);
        var ticket2 = new Ticket(2, 1, 2, 1, 1);
        when(ticketRepository.findBySession(filmSession.getId())).thenReturn(List.of(ticket1, ticket2));

        var actualHallDTO = hallService.findByFilmSession(filmSession.getId());

        assertThat(actualHallDTO).isPresent().get()
                .usingRecursiveComparison()
                .ignoringFields("seats")
                .isEqualTo(hall);
        assertThat(actualHallDTO.get().getSeats()[ticket1.getRowNumber() - 1][ticket1.getPlaceNumber() - 1]).isTrue();
        assertThat(actualHallDTO.get().getSeats()[ticket2.getRowNumber() - 1][ticket2.getPlaceNumber() - 1]).isTrue();
    }

}
