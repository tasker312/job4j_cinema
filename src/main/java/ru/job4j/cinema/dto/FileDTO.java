package ru.job4j.cinema.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    private String name;

    private byte[] content;

}
