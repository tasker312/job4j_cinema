package ru.job4j.cinema.repository.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> book(Ticket ticket);

    Collection<Ticket> findBySession(int sessionId);

    Optional<Ticket> findById(int id);

}
