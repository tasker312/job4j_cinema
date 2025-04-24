package ru.job4j.cinema.service.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface TicketService {

    Optional<Ticket> book(Ticket ticket);

    Optional<Ticket> findById(int id);

}
