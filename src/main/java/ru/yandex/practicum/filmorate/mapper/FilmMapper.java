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
        FilmDto dto = new FilmDto();
        List<Genre> filmGenres = specificFilm.getGenres();

        if (filmGenres != null) {
            List<GenreDto> dtos = filmGenres.stream()
                    .map(GenreMapper::mapToGenreDto)
                    .toList();

            dto.setGenres(dtos);
        }

        dto.setId(specificFilm.getId());
        dto.setName(specificFilm.getName());
        dto.setMpa(MpaMapper.mapToMpaDto(specificFilm.getMpa()));
        dto.setDescription(specificFilm.getDescription());
        dto.setReleaseDate(specificFilm.getReleaseDate());
        dto.setDuration(specificFilm.getDuration());
        dto.setUsersLikes(specificFilm.getUsersLikes());
        return dto;
    }
}
