package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.job4j.cinema.dto.FileDTO;
import ru.job4j.cinema.service.file.FileService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileControllerTest {

    private FileService fileService;

    private FileController fileController;

    private FileDTO fileDTO;

    @BeforeEach
    public void setUp() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
        fileDTO = mock(FileDTO.class);
    }

    @Test
    public void whenRequestFileThenResponseOkWithFile() {
        var expectedResponse = HttpStatus.OK;
        when(fileService.findById(1)).thenReturn(Optional.of(fileDTO));

        var actualResponse = fileController.getFileById(1);

        assertThat(actualResponse.getStatusCode()).isEqualTo(expectedResponse);
    }

    @Test
    public void whenRequestNonExistentFileThenResponseNotFound() {
        var expectedResponse = ResponseEntity.notFound().build();
        when(fileService.findById(1)).thenReturn(Optional.empty());

        var actualResponse = fileController.getFileById(1);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

}
