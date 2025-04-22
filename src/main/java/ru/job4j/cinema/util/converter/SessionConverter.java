package ru.job4j.cinema.util.converter;

import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.dto.FilmSessionDTO;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;

public class SessionConverter {

    public static FilmSessionDTO convert(FilmSession session, FilmDTO film, Hall hall) {
        return FilmSessionDTO.builder()
                .id(session.getId())
                .film(film)
                .hall(hall)
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .price(session.getPrice())
                .build();
    }

}
