package ru.job4j.cinema.dto;

import lombok.*;
import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmSessionDTO {

    private int id;

    private FilmDTO film;

    private Hall hall;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int price;

}
