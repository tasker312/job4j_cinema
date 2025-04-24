package ru.job4j.cinema.service.filmsession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.dto.FilmSessionDTO;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.filmsession.FilmSessionRepository;
import ru.job4j.cinema.service.hall.HallService;
import ru.job4j.cinema.service.film.FilmService;
import ru.job4j.cinema.util.converter.SessionConverter;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;

    private final FilmService filmService;

    private final HallService hallService;

    @Override
    public Optional<FilmSessionDTO> findById(int id) {
        return filmSessionRepository.findById(id)
                .flatMap(session -> filmService.findById(session.getFilmId())
                        .flatMap(filmDTO -> hallService.findById(session.getHallsId())
                                .map(hall -> SessionConverter.convert(session, filmDTO, hall))
                        )
                );
    }

    @Override
    public Collection<FilmSessionDTO> findAll() {
        var sessions = filmSessionRepository.findAll();
        var filmsMap = filmService.findAll().stream()
                .collect(Collectors.toMap(
                        FilmDTO::getId,
                        f -> f
                ));
        var hallsMap = hallService.findAll().stream()
                .collect(Collectors.toMap(
                        Hall::getId,
                        h -> h
                ));
        return sessions.stream()
                .map(session -> SessionConverter.convert(
                                session,
                                filmsMap.get(session.getFilmId()),
                                hallsMap.get(session.getHallsId())
                        )
                )
                .toList();
    }

}
