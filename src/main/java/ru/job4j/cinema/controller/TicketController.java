package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.ticket.TicketService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public String getSuccessPage(@PathVariable int id, Model model, HttpSession session) {
        var ticketOptional = ticketService.findById(id);
        if (ticketOptional.isEmpty()) {
            return "redirect:/sessions";
        }
        var ticket = ticketOptional.get();
        var user = (User) session.getAttribute("user");
        if (user == null || user.getId() != ticket.getUserId()) {
            return "redirect:/sessions";
        }
        model.addAttribute("ticket", ticket);
        return "/tickets/success";
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket) {
        var ticketOptional = ticketService.book(ticket);
        return ticketOptional.map(value -> "redirect:/tickets/" + value.getId())
                .orElse("/tickets/fail");
    }

    @GetMapping("/fail")
    public String getFailPage(Model model) {
        return "/tickets/fail";
    }

}
