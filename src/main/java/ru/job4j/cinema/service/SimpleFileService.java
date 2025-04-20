package ru.job4j.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDTO;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.util.converter.FileConverter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleFileService implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Optional<FileDTO> findById(int id) {
        var file = fileRepository.findById(id);
        return file.map(FileConverter::toFileDTO);
    }

}
