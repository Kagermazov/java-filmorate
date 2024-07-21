package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreDbStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }

    public GenreDto findGenreById(Long id) {
        if (this.storage.findAllGenres().size() < id) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "There's no genre with an id " + id);
        }
        return GenreMapper.mapToGenreDto(this.storage.findGenreById(id)
                .orElseThrow(() -> new InternalServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Null is returned")));
    }

    public List<GenreDto> findAllGenres() {
        return this.storage.findAllGenres()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }
}
