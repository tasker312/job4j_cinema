package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    private TicketController ticketController;

    private TicketService ticketService;

    @BeforeEach
    public void setUp() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenFindNonExistentTicketThenRedirectSessions() {
        when(ticketService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.getSuccessPage(1, model, new MockHttpSession());

        assertThat(view).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenFindTicketThenReturnTicket() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var user = new User(1, "ivan", "ivan@test", "pass");
        when(ticketService.findById(1)).thenReturn(Optional.of(ticket));
        var session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);

        var model = new ConcurrentModel();
        var view = ticketController.getSuccessPage(1, model, session);
        var actualTicket = (Ticket) model.getAttribute("ticket");

        verify(session).getAttribute("user");
        assertThat(actualTicket)
                .usingRecursiveComparison()
                .isEqualTo(ticket);
        assertThat(view).isEqualTo("/tickets/success");
    }

    @Test
    public void whenFindExistentTicketByAnotherUserThenRedirectSessions() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        var user = new User(2, "ivan", "ivan@test", "pass");
        when(ticketService.findById(1)).thenReturn(Optional.of(ticket));
        var session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);

        var model = new ConcurrentModel();
        var view = ticketController.getSuccessPage(1, model, session);

        verify(session).getAttribute("user");
        assertThat(view).isEqualTo("redirect:/sessions");
    }

    @Test
    public void whenFailAfterTicketBookingThenReturnFail() {
        var ticket = new Ticket(1, 1, 1, 1, 1);

        var model = new ConcurrentModel();
        model.addAttribute("ticket", ticket);
        var view = ticketController.getFailPage(model);

        assertThat(view).isEqualTo("/tickets/fail");
    }

    @Test
    public void whenBookTicketThenReturnTicket() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketService.book(ticket)).thenReturn(Optional.of(ticket));

        var view = ticketController.buyTicket(ticket);

        verify(ticketService).book(ticket);
        assertThat(view).isEqualTo("redirect:/tickets/" + ticket.getId());
    }

    @Test
    public void whenBookTicketForOccupiedPlaceThenReturnFail() {
        var ticket = new Ticket(1, 1, 1, 1, 1);
        when(ticketService.book(ticket)).thenReturn(Optional.empty());

        var view = ticketController.buyTicket(ticket);

        assertThat(view).isEqualTo("/tickets/fail");
    }

}
