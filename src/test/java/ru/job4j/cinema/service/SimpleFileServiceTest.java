package ru.job4j.cinema.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FileDTO;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.file.FileRepository;
import ru.job4j.cinema.service.file.FileService;
import ru.job4j.cinema.service.file.SimpleFileService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleFileServiceTest {

    private FileService fileService;

    private FileRepository fileRepository;

    @BeforeEach
    public void setup() {
        fileRepository = mock(FileRepository.class);
        fileService = new SimpleFileService(fileRepository);
    }

    @Test
    public void whenFindFileByIdThenReturnFileDTO() {
        var file = new File(1, "name", "path");
        when(fileRepository.findById(1)).thenReturn(Optional.of(file));

        var fileDTO = fileService.findById(1);

        assertThat(fileDTO).isPresent().get()
                .extracting(FileDTO::getName)
                .isEqualTo(file.getName());

    }

    @Test
    public void whenFindNonExistentFileThenOptionalEmpty() {
        when(fileRepository.findById(1)).thenReturn(Optional.empty());

        var fileDTO = fileService.findById(1);

        assertThat(fileDTO).isEmpty();
    }

}
