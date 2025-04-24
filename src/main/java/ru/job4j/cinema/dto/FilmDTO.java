package ru.job4j.cinema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDTO {

    private int id;

    private String name;

    private String description;

    private int year;

    private String genre;

    private int minimalAge;

    private int durationInMinutes;

    private int fileId;

}
