package ru.job4j.cinema.service.hall;

import ru.job4j.cinema.dto.HallDTO;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

public interface HallService {

    Optional<Hall> findById(int id);

    Collection<Hall> findAll();

    Optional<HallDTO> findByFilmSession(int sessionId);

}
