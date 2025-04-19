package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/films")
public class FilmController {

    @GetMapping("")
    public String getListPage() {
        return "/films/list";
    }

    @GetMapping("/{id}")
    public String getFilmPage(@PathVariable String id) {
        return "/films/_id";
    }

}
