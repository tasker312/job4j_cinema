package ru.job4j.cinema.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Hall {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "name", "name",
            "row_count", "rowCount",
            "place_count", "placeCount",
            "description", "description"
    );

    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private int rowCount;

    private int placeCount;

    private String description;

}
