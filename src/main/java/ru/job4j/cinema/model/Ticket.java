package ru.job4j.cinema.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "session_id", "sessionId",
            "row_number", "rowNumber",
            "place_number", "placeNumber",
            "user_id", "userId"
    );

    @EqualsAndHashCode.Include
    private int id;

    private int sessionId;

    private int rowNumber;

    private int placeNumber;

    private int userId;

}
