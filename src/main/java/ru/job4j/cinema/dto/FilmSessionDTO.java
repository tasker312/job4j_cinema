package ru.job4j.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

@Data
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
