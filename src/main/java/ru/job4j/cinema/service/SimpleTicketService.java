package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Optional<Ticket> book(Ticket ticket) {
        return ticketRepository.book(ticket);
    }

}
