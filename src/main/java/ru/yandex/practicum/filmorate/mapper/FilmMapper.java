package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class FilmMapper {

    private FilmMapper() {
    }

    public static FilmDto mapToFilmDto(Film specificFilm) {
        FilmDto dto = FilmDto.builder()
                .id(specificFilm.getId())
                .name(specificFilm.getName())
                .mpa(MpaMapper.mapToMpaDto(specificFilm.getMpa()))
                .description(specificFilm.getDescription())
                .releaseDate(specificFilm.getReleaseDate())
                .duration(specificFilm.getDuration())
                .usersLikes(specificFilm.getUsersLikes())
                .build();

        List<Genre> filmGenres = specificFilm.getGenres();

        if (filmGenres != null) {
            List<GenreDto> dtos = filmGenres.stream()
                    .map(GenreMapper::mapToGenreDto)
                    .toList();

            dto.setGenres(dtos);
        }

        return dto;
    }
}
