package ru.job4j.cinema.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class FilmSession {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "film_id", "filmId",
            "halls_id", "hallsId",
            "start_time", "startTime",
            "end_time", "endTime",
            "price", "price"
    );

    @EqualsAndHashCode.Include
    private int id;

    private int filmId;

    private int hallsId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int price;

}
