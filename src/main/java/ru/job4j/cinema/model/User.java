package ru.job4j.cinema.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "full_name", "fullName",
            "email", "email",
            "password", "password"
    );

    @EqualsAndHashCode.Include
    private int id;

    private String fullName;

    @NotBlank
    @EqualsAndHashCode.Include
    private String email;

    @NotBlank
    private String password;

}
