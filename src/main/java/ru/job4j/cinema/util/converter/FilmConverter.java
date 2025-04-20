package ru.job4j.cinema.util.converter;

import ru.job4j.cinema.dto.FilmDTO;
import ru.job4j.cinema.model.Film;

public class FilmConverter {

    public static FilmDTO toFilmDTO(Film film, String genre) {
        return FilmDTO.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .year(film.getYear())
                .genre(genre)
                .minimalAge(film.getMinimalAge())
                .durationInMinutes(film.getDurationInMinutes())
                .fileId(film.getFileId())
                .build();
    }

}
