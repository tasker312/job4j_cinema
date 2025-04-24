package ru.job4j.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.film.FilmService;

@Controller
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("")
    public String getListPage(Model model) {
        var films = filmService.findAll();
        model.addAttribute("films", films);
        return "/films/list";
    }

    @GetMapping("/{id}")
    public String getFilmPage(@PathVariable int id, Model model) {
        var filmOptional = filmService.findById(id);
        if (filmOptional.isEmpty()) {
            return "redirect:/films/list";
        }
        model.addAttribute("film", filmOptional.get());
        return "/films/_id";
    }

}
