package ru.job4j.cinema.service.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Optional<Ticket> book(Ticket ticket) {
        if (ticket.getRowNumber() < 1 || ticket.getPlaceNumber() < 1) {
            return Optional.empty();
        }
        return ticketRepository.book(ticket);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

}
