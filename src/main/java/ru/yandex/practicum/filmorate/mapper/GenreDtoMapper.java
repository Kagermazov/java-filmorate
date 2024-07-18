package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreDtoMapper {

    private GenreDtoMapper() {
    }

    public static GenreDto mapToGenreDto(Genre specificGenre) {
        GenreDto newGenreDto = new GenreDto();

        newGenreDto.setId(specificGenre.getId());
        newGenreDto.setName(specificGenre.getName());
        return newGenreDto;
    }
}
