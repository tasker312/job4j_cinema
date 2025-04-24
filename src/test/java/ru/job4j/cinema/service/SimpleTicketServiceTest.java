package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;
import ru.job4j.cinema.service.ticket.SimpleTicketService;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTicketServiceTest {

    private TicketService ticketService;

    private TicketRepository ticketRepository;

    @BeforeEach
    public void setup() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new SimpleTicketService(ticketRepository);
    }

    @Test
    public void whenBookTicketThenReturnTicket() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketRepository.book(ticket)).thenReturn(Optional.of(ticket));

        var actualTicket = ticketService.book(ticket);

        assertThat(actualTicket).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(ticket);
    }

    @Test
    public void whenTicketRowBelowZeroThenReturnEmpty() {
        var ticket = new Ticket(1, 1, 0, 1, 1);

        var actualTicket = ticketService.book(ticket);

        assertThat(actualTicket).isEmpty();
    }

    @Test
    public void whenTicketPlaceBelowZeroThenReturnEmpty() {
        var ticket = new Ticket(1, 1, 1, -1, 1);

        var actualTicket = ticketService.book(ticket);

        assertThat(actualTicket).isEmpty();
    }

    @Test
    public void whenFindByIdThenReturnTicket() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));

        var actualTicket = ticketService.findById(1);

        assertThat(actualTicket).isPresent().get()
                .usingRecursiveComparison()
                .isEqualTo(ticket);
    }

    @Test
    public void whenFindNonExistentTicketThenReturnEmpty() {
        when(ticketRepository.findById(1)).thenReturn(Optional.empty());

        var actualTicket = ticketService.findById(1);

        assertThat(actualTicket).isEmpty();
    }

}
