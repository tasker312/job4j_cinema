package ru.job4j.cinema.service.hall;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.HallDTO;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.filmsession.FilmSessionRepository;
import ru.job4j.cinema.repository.hall.HallRepository;
import ru.job4j.cinema.repository.ticket.TicketRepository;
import ru.job4j.cinema.util.converter.HallConverter;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;

    private final TicketRepository ticketRepository;

    private final FilmSessionRepository filmSessionRepository;

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public Collection<Hall> findAll() {
        return hallRepository.findAll();
    }

    @Override
    public Optional<HallDTO> findByFilmSession(int sessionId) {
        var session = filmSessionRepository.findById(sessionId);
        var hall = hallRepository.findById(session.get().getHallsId());
        if (hall.isEmpty()) {
            return Optional.empty();
        }
        var tickets = ticketRepository.findBySession(session.get().getId());
        return Optional.ofNullable(HallConverter.convert(hall.get(), tickets));
    }

}
