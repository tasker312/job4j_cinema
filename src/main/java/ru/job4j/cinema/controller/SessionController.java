package ru.job4j.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;

@Controller
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final FilmSessionService sessionService;

    private final HallService hallService;

    @GetMapping("")
    public String getSessionsList(Model model) {
        var sessions = sessionService.findAll();
        model.addAttribute("filmSessions", sessions);
        return "/sessions/list";
    }

    @GetMapping("/{id}")
    public String getSessionPage(@PathVariable int id, Model model) {
        var session = sessionService.findById(id);
        if (session.isEmpty()) {
            return "redirect:/sessions";
        }
        model.addAttribute("filmSession", session.get());
        var hall = hallService.findByFilmSession(id);
        model.addAttribute("hall", hall.orElse(null));
        return "/sessions/_id";
    }

}
