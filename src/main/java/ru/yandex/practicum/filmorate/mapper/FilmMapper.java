package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaForFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public class FilmMapper {

    private FilmMapper() {
    }

    public static FilmDto mapToFilmDto(Film specificFilm) {
        FilmDto dto = new FilmDto();
        List<Genre> filmGenres = specificFilm.getGenres();
        List<GenreDto> genreDtos = null;

        if (filmGenres != null) {
            genreDtos = specificFilm.getGenres().stream()
                    .map(GenreDtoMapper::mapToGenreDto)
                    .toList();
        }

        dto.setId(specificFilm.getId());
        dto.setName(specificFilm.getName());
        dto.setMpa(new MpaForFilmDto(specificFilm.getMpa().getId()));
        dto.setDescription(specificFilm.getDescription());
        dto.setReleaseDate(specificFilm.getReleaseDate());
        dto.setDuration(specificFilm.getDuration());
        dto.setGenres(genreDtos);
        dto.setUsersLikes(specificFilm.getUsersLikes());
        return dto;
    }
}
