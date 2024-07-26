package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    private GenreMapper() {
    }

    public static GenreDto mapToGenreDto(Genre specificGenre) {

        if (specificGenre == null) {
            return null;
        }

        return GenreDto.builder()
                .id(specificGenre.getId())
                .name(specificGenre.getName())
                .build();
    }
}
