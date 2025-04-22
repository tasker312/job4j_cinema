package ru.job4j.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
