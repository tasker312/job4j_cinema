package ru.job4j.cinema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallDTO {

    private int id;

    private String name;

    private int rowCount;

    private int placeCount;

    private String description;

    private boolean[][] seats;

}
