package ru.job4j.cinema.util.converter;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.cinema.dto.FileDTO;
import ru.job4j.cinema.model.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileConverter {

    public static FileDTO toFileDTO(File file) {
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(Path.of(file.getPath()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new FileDTO(file.getName(), fileContent);
    }

}
